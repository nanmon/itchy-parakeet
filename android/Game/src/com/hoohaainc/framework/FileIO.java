package com.hoohaainc.framework;

import android.content.SharedPreferences;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by nancio on 10/07/15.
 */
public interface FileIO {
    public InputStream readFile(String file) throws IOException;
    public OutputStream writeFile(String file) throws IOException;
    public InputStream readAsset(String file) throws  IOException;
    public SharedPreferences getSharedPref();
}
