package com.hoohaainc.framework.implementation;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import com.hoohaainc.framework.Audio;
import com.hoohaainc.framework.Music;
import com.hoohaainc.framework.Sound;

import java.io.IOException;

/**
 * Created by nancio on 11/07/15.
 */
public class AndroidAudio implements Audio{
    AssetManager assets;
    SoundPool soundPool;

    public AndroidAudio(Activity activity){
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        this.assets = activity.getAssets();
        this.soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
    }


    @Override
    public Music createMusic(String file) {
        try{
            AssetFileDescriptor assetDescriptor = assets.openFd(file);
            return new AndroidMusic(assetDescriptor);
        }catch (IOException e){
            throw new RuntimeException("Could not load music '" + file + "'");
        }
    }

    @Override
    public Sound createSound(String file) {
        try{
            AssetFileDescriptor assetDescriptor = assets.openFd(file);
            int soundId = soundPool.load(assetDescriptor, 0);
            return new AndroidSound(soundPool, soundId);
        }catch (IOException e){
            throw new RuntimeException("Could not load music '" + file + "'");
        }
    }
}
