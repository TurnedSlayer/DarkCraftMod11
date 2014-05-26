package codechicken.microblock

import codechicken.lib.vec.Cuboid6
import codechicken.lib.vec.Scale
import codechicken.lib.vec.Vector3
import codechicken.lib.vec.TransformationList
import codechicken.lib.vec.AxisCycle
import codechicken.lib.vec.Rotation
import Vector3._
import Rotation._
import codechicken.multipart.TMultiPart
import codechicken.multipart.TNormalOcclusion
import codechicken.multipart.JPartialOcclusion
import codechicken.multipart.TileMultipart
import codechicken.multipart.MultiPartRegistry
import codechicken.microblock.MicroMaterialRegistry.IMicroMaterial
import codechicken.lib.raytracer.ExtendedMOP
import codechicken.multipart.TEdgePart
import scala.collection.JavaConversions._
import codechicken.lib.data.MCDataInput
import codechicken.lib.lighting.LightMatrix

object EdgePlacement extends PlacementProperties
{
    import codechicken.multipart.PartMap._
    
    def microClass = EdgeMicroClass
    
    def placementGrid = EdgePlacementGrid
    
    def opposite(slot:Int, side:Int):Int = 
    {
        if(slot < 0)//custom placement
            return slot
        val e = slot-15
        return 15+ packEdgeBits(e, unpackEdgeBits(e)^(1<<(side>>1)))
    }
    
    override def customPlacement(pmt:MicroblockPlacement):ExecutablePlacement = 
    {
        if(pmt.size%2 == 1) return null
        
        val part = PostMicroClass.create(pmt.size, pmt.hit.sideHit>>1, pmt.material, pmt.world.isRemote)
        if(pmt.doExpand)
        {
            val hpart = pmt.htile.partList(ExtendedMOP.getData[(Int, _)](pmt.hit)._1)
            if(hpart.getType == PostMicroClass.getName)
            {
                val mpart = hpart.asInstanceOf[Microblock]
                if(mpart.material == pmt.material && mpart.getSize + pmt.size < 8)
                {
                    part.shape = ((mpart.getSize+pmt.size)<<4|mpart.getShape).toByte
                    return pmt.expand(mpart, part)
                }
            }
        }
        
        if(pmt.slot >= 0)
            return null
        
        if(pmt.internal && !pmt.oppMod)
            return pmt.internalPlacement(pmt.htile.asInstanceOf[TileMultipart], part)
        
        return pmt.externalPlacement(part)
    }
}

object EdgeMicroClass extends MicroblockClass
{
    var aBounds:Array[Cuboid6] = new Array(256)
    
    for(s <- 0 until 12)
    {
        val rx = if((s&2) != 0) -1 else 1
        val rz = if((s&1) != 0) -1 else 1
        val transform = new TransformationList(new Scale(new Vector3(rx, 1, rz)), AxisCycle.cycles(s>>2)).at(center)
        
        for(t <- 1 until 8)
        {
            val d = t/8D
            aBounds(t<<4|s) = new Cuboid6(0, 0, 0, d, 1, d).apply(transform)
        }
    }
    
    override def itemSlot = 15
    
    def getName = "mcr_edge"
    
    def create(client:Boolean) = 
        if(client)
            new EdgeMicroblockClient
        else
            new EdgeMicroblock
    
    def create(size:Int, slot:Int, material:Int, client:Boolean) = 
        if(client)
            new EdgeMicroblockClient(size, slot, material)
        else
            new EdgeMicroblock(size, slot, material)
    
    def placementProperties = EdgePlacement

    def getResistanceFactor = 0.5F
}

class EdgeMicroblock(shape$:Byte = 0, material$:Int = 0) extends CommonMicroblock(shape$, material$) with TEdgePart
{
    def this(size:Int, slot:Int, material:Int) = this((size<<4|(slot-15)).toByte, material)
    
    def microClass = EdgeMicroClass
    
    def getBounds = EdgeMicroClass.aBounds(shape)
    
    override def getSlot = getShape+15
}

class EdgeMicroblockClient(shape$:Byte = 0, material$:Int = 0) extends EdgeMicroblock(shape$, material$) with CommonMicroblockClient
{
    def this(size:Int, slot:Int, material:Int) = this((size<<4|(slot-15)).toByte, material)
}

object PostMicroClass
{
    var aBounds:Array[Cuboid6] = new Array(256)
    
    for(s <- 0 until 3)
    {
        val transform = sideRotations(s<<1).at(center)
        for(t <- 2 until 8 by 2)
        {
            val d1 = 0.5-t/16D
            val d2 = 0.5+t/16D
            aBounds(t<<4|s) = new Cuboid6(d1, 0, d1, d2, 1, d2).apply(transform)
        }
    }
    
    def getName = "mcr_post"
    
    def create(client:Boolean) = 
        if(client)
            new PostMicroblockClient
        else
            new PostMicroblock
    
    def create(size:Int, slot:Int, material:Int, client:Boolean) = 
        if(client)
            new PostMicroblockClient(size, slot, material)
        else
            new PostMicroblock(size, slot, material)
    
    def register() = MultiPartRegistry.registerParts((_, c:Boolean) => create(c), getName)

    def getResistanceFactor = 0.5F
}

class PostMicroblockClient(shape$:Byte = 0, material$:Int = 0) extends PostMicroblock(shape$, material$) with MicroblockClient
{
    var renderBounds1:Cuboid6 = _
    var renderBounds2:Cuboid6 = _
    
    def this(size:Int, orient:Int, material:Int) = this((size<<4|orient).toByte, material)
    
    override def render(pos:Vector3, pass:Int)
    {
        val mat = getIMaterial
        if(pass == -1)
            MicroblockRender.renderCuboid(pos, mat, pass, getBounds, 0)
        else {
            MicroblockRender.renderCuboid(pos, mat, pass, renderBounds1, 0)
            if (renderBounds2 != null)
                MicroblockRender.renderCuboid(pos, mat, pass, renderBounds2, 0)
        }
    }
    
    override def onPartChanged(part:TMultiPart)
    {
        recalcBounds()
    }
    
    override def onAdded()
    {
        super.onAdded()
        recalcBounds()
    }

    override def read(packet:MCDataInput)
    {
        super.read(packet)
        recalcBounds()
    }
    
    def recalcBounds()
    {
        renderBounds1 = getBounds.copy
        renderBounds2 = null
        
        shrinkFace(getShape<<1)
        shrinkFace(getShape<<1|1)
        
        tile.partList.foreach(p =>
            if(p.isInstanceOf[PostMicroblock] && p != this)
                shrinkPost(p.asInstanceOf[PostMicroblock])
        )
    }
    
    def shrinkFace(fside:Int)
    {
        val part = tile.partMap(fside)
        if(part != null && part.getType.equals("mcr_face"))
            MicroOcclusion.shrink(renderBounds1, part.asInstanceOf[CommonMicroblock].getBounds, fside)
    }
    
    def shrinkPost(post:PostMicroblock)
    {
        if(post == this)
            return
        
        if(thisShrinks(post))
        {
            if(renderBounds2 == null)
                renderBounds2 = renderBounds1.copy
            else
                renderBounds2.set(renderBounds1)
            MicroOcclusion.shrink(renderBounds1, post.getBounds, getShape<<1|1)
            MicroOcclusion.shrink(renderBounds2, post.getBounds, getShape<<1)
        }
    }
    
    def thisShrinks(other:PostMicroblock):Boolean =
    {
        if(getSize != other.getSize) return getSize < other.getSize
        if(isTransparent != other.isTransparent) return isTransparent
        return getShape > other.getShape
    }
}

class PostMicroblock(shape$:Byte = 0, material$:Int = 0) extends Microblock(shape$, material$) with JPartialOcclusion with TNormalOcclusion
{
    def this(size:Int, orient:Int, material:Int) = this((size<<4|orient).toByte, material)
    
    override def getType = PostMicroClass.getName
    
    def getBounds = PostMicroClass.aBounds(shape)
    
    def getOcclusionBoxes = Seq(getBounds)
    
    def getPartialOcclusionBoxes = getOcclusionBoxes
    
    override def itemClassID = EdgeMicroClass.classID
    
    override def occlusionTest(npart:TMultiPart): Boolean = 
    {
        if(npart.isInstanceOf[PostMicroblock])
            return npart.asInstanceOf[PostMicroblock].getShape != getShape
        
        if(npart.getType.equals("mcr_face"))
            if(npart.asInstanceOf[CommonMicroblock].getSlot>>1 == getShape)
                return true

        return super.occlusionTest(npart)
    }

    def getResistanceFactor = PostMicroClass.getResistanceFactor
}