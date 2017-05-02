package br.com.passalixo.projetoandroid.passalixo.Config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Fernanda on 20/04/2017.
 */

public final class ConfiguracaoFirebase {

    private static DatabaseReference referenciaFirebase;
    private static FirebaseAuth autentificacao;

    public static DatabaseReference getFirebase(){

        if(referenciaFirebase == null) {
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }
        return referenciaFirebase;
    }

    public static FirebaseAuth getFirebaseAutentificacao(){
        if( autentificacao == null){
            autentificacao = FirebaseAuth.getInstance();
        }
        return autentificacao;
    }
}
