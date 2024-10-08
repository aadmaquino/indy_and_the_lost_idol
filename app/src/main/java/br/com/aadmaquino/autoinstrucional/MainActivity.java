package br.com.aadmaquino.autoinstrucional;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

/**
* Classe MainActivity para a atividade activity_main.xml
*
* @author Antônio Augusto Duarte Moura de Aquino
* @author Iago Ávila Duarte
* @version 1.0
* @since 17/06/2015
*/
public class MainActivity extends Activity {
    private MediaPlayer raidersmarch;

    /**
    * Método onCreate da classe {@link MainActivity} para a atividade activity_main.xml
    *
    * @since 17/06/2015
    * @param savedInstanceState - savedInstanceState
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Deixa a tela da atividade sempre ligada
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Criação das variáveis para definir um tipo de font-family
        Typeface adventure = Typeface.createFromAsset(getAssets(), "adventure.ttf");
        Typeface adventurehollow = Typeface.createFromAsset(getAssets(), "adventurehollow.ttf");

        // Criação das variáveis para terem acesso aos componentes gráficos
        Button help = (Button) findViewById(R.id.ButtonHelp);
        Button play = (Button) findViewById(R.id.ButtonPlay);

        // Definição das fontes
        help.setTypeface(adventurehollow);
        play.setTypeface(adventure);

        // Música Tema
        raidersmarch = MediaPlayer.create(this, R.raw.raiders_march);
        raidersmarch.setLooping(true);
    }

    /**
    * Método onClickHelp da classe {@link MainActivity} para o botão "Ajuda" da atividade activity_main.xml
    *
    * @since 17/06/2015
    * @param view - View
    */
    public void OnClickHelp(View view){
        // Pausa a música tema
        raidersmarch.pause();

        // Chama a atividade activity_help.xml
        Intent intentHelp = new Intent(this, HelpActivity.class);
        startActivity(intentHelp);
    }

    /**
    * Método onClickPlay da classe {@link MainActivity} para o botão "Jogar" da atividade activity_main.xml
    *
    * @since 17/06/2015
    * @param view - View
    */
    public void OnClickPlay(View view){
        // Pausa a música tema
        raidersmarch.pause();

        // Chama a atividade activity_gameplay.xml
        Intent intentPlay = new Intent(this, GamePlayActivity.class);
        startActivity(intentPlay);
    }

    /**
    * Método onStart da classe {@link MainActivity} para a atividade activity_main.xml
    *
    * @since 17/06/2015
    */
    @Override
    protected void onStart() {
        super.onStart();
        // Inicia a música tema
        raidersmarch.start();
    }

    /**
    * Método onResume da classe {@link MainActivity} para a atividade activity_main.xml
    *
    * @since 17/06/2015
    */
    @Override
    protected void onResume() {
        super.onResume();
        // Define a posição atual da música tema quando ela foi pausada anteriormente
        raidersmarch.seekTo(raidersmarch.getCurrentPosition());
    }

    /**
    * Método onPause da classe {@link MainActivity} para a atividade activity_main.xml
    *
    * @since 17/06/2015
    */
    @Override
    protected void onPause() {
        super.onPause();
        // Pausa a música tema
        raidersmarch.pause();
    }

    /**
    * Método onBackPressed da classe {@link MainActivity} para a atividade activity_main.xml
    *
    * @since 17/06/2015
    */
    @Override
    public void onBackPressed() {
        // Se o usuário apertar a tecla "Voltar" do dispositivo, o método doExit será chamado.
        doExit();
    }

    /**
    * Método onExit da classe {@link MainActivity} para a atividade activity_main.xml
    *
    * @since 17/06/2015
    */
    protected void doExit() {
        // Exibe um AlertDialog perguntando se o usuário deseja sair do jogo
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.action_exit);
        builder.setMessage(R.string.action_exit_message);
        builder.setPositiveButton(R.string.button_yes_message, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton(R.string.button_no_message, null);
        builder.show();
    }
}
