package com.github.donkirkby.vograbulary;

import com.badlogic.gdx.tools.imagepacker.TexturePacker2;

public class AssetPacker {
    public static void main(String[] args) throws Exception {
        TexturePacker2.process(
                "assets-raw/skin", 
                "../vograbulary-android/assets/data/ui", 
                "uiskin.atlas");
    }
}
