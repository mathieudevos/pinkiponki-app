package com.mattikettu.pinkiponki.util;

/**
 * Created by mathieu on 10/06/2016.
 */


import dagger.Module;

@Module(
        includes = {
                AndroidModule.class,
                BootstrapModule.class
        }
)

public class RootModule {}


