package com.itzb.opengltest;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TextResourceReader {
    public static String readTextFileFromResource(Context context, int resId) {
        StringBuffer body = new StringBuffer();
        try {
            InputStream inputStream = context.getResources().openRawResource(resId);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                body.append(line);
                body.append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("could not open resource: " + resId, e);
        } catch (Resources.NotFoundException e) {
            throw new RuntimeException("resource not found: " + resId, e);
        }
        return body.toString();
    }
}
