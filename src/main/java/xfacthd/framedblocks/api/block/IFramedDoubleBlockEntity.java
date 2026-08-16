package xfacthd.framedblocks.api.block;

import xfacthd.framedblocks.api.camo.CamoContainer;

public interface IFramedDoubleBlockEntity
{
    String CAMO_TWO_NBT_KEY = "camo_two";

    CamoContainer getCamoTwo();
}
