package br.com.passalixo.projetoandroid.passalixo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.passalixo.projetoandroid.passalixo.Config.ConfiguracaoFirebase;
import br.com.passalixo.projetoandroid.passalixo.Helper.Base64Custom;
import br.com.passalixo.projetoandroid.passalixo.Helper.Preferencias;
import br.com.passalixo.projetoandroid.passalixo.Model.Usuario;

public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText senha;
    private Button login_bt;
    private ImageButton fb_login;
    private ImageButton google_login;
    private TextView recupera_senha;
    private TextView novo_usuario;

    private Usuario usuario;

    private FirebaseAuth autenticacao;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerUsuario;
    private String identificadorUsuarioLogado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(android.R.layout.activity_main);

        email = (EditText)findViewById(R.id.email_id);
        senha = (EditText)findViewById(R.id.senha_id);
        login_bt = (Button)findViewById(R.id.botao_id);
        fb_login = (ImageButton)findViewById(R.id.botao_fb_id);
        google_login = (ImageButton) findViewById(R.id.google_id);
        recupera_senha = (TextView) findViewById(R.id.recupera_senha_id);
        novo_usuario = (TextView) findViewById(R.id.registrar_id);

        login_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario = new Usuario();
                usuario.setEmail( email.getText().toString() );
                usuario.setSenha( senha.getText().toString() );
                validarLogin();

            }
        });

        novo_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                abrirNovoUsuario();

            }
                });
    }

    private void validarLogin(){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutentificacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if( task.isSuccessful() ){


                    identificadorUsuarioLogado = Base64Custom.codificarBase64(usuario.getEmail());

                    firebase = ConfiguracaoFirebase.getFirebase()
                            .child("usuarios")
                            .child( identificadorUsuarioLogado );

                    valueEventListenerUsuario = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Usuario usuarioRecuperado = dataSnapshot.getValue( Usuario.class );

                            Preferencias preferencias = new Preferencias(MainActivity.this);
                            preferencias.salvarDados( identificadorUsuarioLogado, usuarioRecuperado.getNome() );

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };

                    firebase.addListenerForSingleValueEvent( valueEventListenerUsuario );



                    abrirTelaPrincipal();
                    Toast.makeText(MainActivity.this, "Sucesso ao fazer login!", Toast.LENGTH_LONG ).show();
                }else{
                    Toast.makeText(MainActivity.this, "Erro ao fazer login!", Toast.LENGTH_LONG ).show();
                }

            }
        });
    }

    private void abrirTelaPrincipal(){
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity( intent );
        finish();
    }

    private void verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutentificacao();
        if( autenticacao.getCurrentUser() != null ){
            abrirTelaPrincipal();
        }
    }

    public void abrirNovoUsuario(View view){
        Intent intent = new Intent(MainActivity.this, CadastroUsuario.class);
        startActivity(intent);
    }


}
