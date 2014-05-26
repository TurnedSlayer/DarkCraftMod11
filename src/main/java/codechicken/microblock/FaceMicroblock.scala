package codechicken.microblock

import codechicken.lib.vec.Cuboid6
import codechicken.multipart.TFacePart
import codechicken.lib.vec.Vector3
import codechicken.lib.vec.Rotation
import codechicken.microblock.MicroMaterialRegistry.IMicroMaterial
import Rotation._
import Vector3._
import codechicken.lib.lighting.LightMatrix

object FacePlacement extends PlacementProperties
{
    def microClass = FaceMicroClass
    
    def placementGrid = FacePlacementGrid
    
    def opposite(slot:Int, side:Int) = slot^1
    
    override def expand(slot:Int, side:Int) = sneakOpposite(slot, side)
    
    override def sneakOpposite(slot:Int, side:Int) = slot == (side^1)
}

object FaceMicroClass extends MicroblockClass
{
    var aBounds:Array[Cuboid6] = new Array(256)
    
    for(s <- 0 until 6)
    {
        val transform = sideRotations(s).at(center)
        for(t <- 1 until 8)
        {
            val d = t/8D
            aBounds(t<<4|s) = new Cuboid6(0, 0, 0, 1, d, 1).apply(transform)
        }
    }
    
    def getName = "mcr_face"
    
    def create(client:Boolean) = 
        if(client)
            new FaceMicroblockClient
        else
            new FaceMicroblock
    
    def create(size:Int, slot:Int, material:Int, client:Boolean) = 
        if(client)
            new FaceMicroblockClient(size, slot, material)
        else
            new FaceMicroblock(size, slot, material)
    
    def placementProperties = FacePlacement

    def getResistanceFactor = 1
}

class FaceMicroblockClient(shape$:Byte = 0, material$:Int = 0) extends FaceMicroblock(shape$, material$) with CommonMicroblockClient
{
    def this(size:Int, shape:Int, material:Int) = this((size<<4|shape).toByte, material)

    override def render(pos:Vector3, pass:Int) {
        if(pass < 0)
            MicroblockRender.renderCuboid(pos, getIMaterial, pass, getBounds, 0)
        else if(isTransparent)
            MicroblockRender.renderCuboid(pos, getIMaterial, pass, renderBounds, renderMask)
        else {
            val mat = getIMaterial
            MicroblockRender.renderCuboid(pos, mat, pass, renderBounds, renderMask | 1<<getSlot)
            MicroblockRender.renderCuboid(pos, mat, pass, Cuboid6.full, ~(1<<getSlot))
        }
    }
}

class FaceMicroblock(shape$:Byte = 0, material$:Int = 0) extends CommonMicroblock(shape$, material$) with TFacePart
{
    def this(size:Int, shape:Int, material:Int) = this((size<<4|shape).toByte, material)
    
    def microClass = FaceMicroClass
    
    def getBounds = FaceMicroClass.aBounds(shape)
    
    override def solid(side:Int) = getIMaterial.isSolid
}
