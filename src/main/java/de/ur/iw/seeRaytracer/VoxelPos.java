package de.ur.iw.seeRaytracer;

public class VoxelPos {

    private int x;
    private int y;
    private int z;

    public VoxelPos(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getZ(){
        return z;
    }

    public VoxelPos add(VoxelPos vp){
        return this.addX(vp.x).addY(vp.y).addZ(vp.z);
    }

    @SuppressWarnings("WeakerAccess")
    public VoxelPos addX(int x){
        return new VoxelPos(this.x + x, this.y, this.z);
    }

    @SuppressWarnings("WeakerAccess")
    public VoxelPos addY(int y){
        return new VoxelPos(this.x, this.y + y, this.z);
    }

    @SuppressWarnings("WeakerAccess")
    public VoxelPos addZ(int z){
        return new VoxelPos(this.x, this.y, this.z + z);
    }

    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(o == null){
            return false;
        }
        if(getClass() != o.getClass()){
            return false;
        }
        VoxelPos v = (VoxelPos)o;
        boolean result = x == v.x;
        result &= y == v.y;
        result &= z == v.z;
        return result;
    }


}
