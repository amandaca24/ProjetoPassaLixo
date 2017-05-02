package br.com.passalixo.projetoandroid.passalixo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import br.com.passalixo.projetoandroid.passalixo.Config.ConfiguracaoFirebase;
import br.com.passalixo.projetoandroid.passalixo.Model.Usuario;

public class CadastroUsuario extends AppCompatActivity {

    private EditText nome;
    private EditText email;
    private EditText endereco;
    private EditText cep;
    private EditText nomeUsuario;
    private EditText senha;
    private Button btCadastrar;

    private Usuario usuario;

    private FirebaseAuth autentificacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        nome = (EditText) findViewById(R.id.idNome);
        email = (EditText) findViewById(R.id.idEmail);
        endereco = (EditText) findViewById(R.id.idEnd);
        cep = (EditText) findViewById(R.id.idCEP);
        nomeUsuario = (EditText) findViewById(R.id.idUsuario);
        senha = (EditText) findViewById(R.id.idSenha);
        btCadastrar = (Button) findViewById(R.id.btCadastrar);

        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario = new Usuario();
                usuario.setNome(nome.getText().toString());
                usuario.setEmail(email.getText().toString());
                usuario.setEndereco(endereco.getText().toString());
                usuario.setCep(cep.getText().toString());
                usuario.setNomeUsuario(nomeUsuario.getText().toString());
                usuario.setSenha(senha.getText().toString());

                cadastrarUsuario();
            }
        });
    }

    private void cadastrarUsuario(){
        autentificacao = ConfiguracaoFirebase.getFirebaseAutentificacao();
        autentificacao.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(CadastroUsuario.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(CadastroUsuario.this, "Sucesso ao cadastrar o usuário", Toast.LENGTH_LONG).show();

                            FirebaseUser usuarioFirebase = task.getResult().getUser();
                            usuario.setId( usuarioFirebase.getUid());
                            usuario.Salvar();

                            autentificacao.signOut();
                            finish();

                        }else{

                            String erroExcessao = "";

                            try{
                                throw  task.getException();

                            }catch (FirebaseAuthWeakPasswordException e){
                                erroExcessao = "Digite uma senha mais forte";
                            }catch (FirebaseAuthInvalidCredentialsException e){
                                erroExcessao = "Digite um e-mail válido";

                            }catch (FirebaseAuthUserCollisionException e){
                                erroExcessao = "Esse e-mail já existe";

                            }catch (Exception e){
                                erroExcessao = "Erro ao efetuar o cadastro";
                                e.printStackTrace();

                            }
                            Toast.makeText(CadastroUsuario.this, "Erro: " + erroExcessao, Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}
