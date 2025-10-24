package xyz.yfrostyf.toxony.client.utils;

import com.mojang.blaze3d.Blaze3D;

public class ClientUtil {
    public static int getClientTick(){
        return (int)(Blaze3D.getTime() * 20d);
    }
}
