package br.com.passalixo.projetoandroid.passalixo.Helper;

import android.util.Base64;

/**
 * Created by Home-CA on 01/05/2017.
 */

public class Base64Custom {

    public static String codificarBase64(String texto){
        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)","");
    }

    public static String decodificarBase64(String textoCodificado){
        return new String( Base64.decode(textoCodificado, Base64.DEFAULT ) );
    }
}
