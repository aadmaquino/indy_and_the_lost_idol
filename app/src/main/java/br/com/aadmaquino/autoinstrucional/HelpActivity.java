package br.com.aadmaquino.autoinstrucional;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
* Classe HelpActivity para a atividade activity_help.xml
*
* @author Antônio Augusto Duarte Moura de Aquino
* @author Iago Ávila Duarte
* @version 1.0
* @since 17/06/2015
*/
public class HelpActivity extends Activity {
    private MediaPlayer helpmusic;

    /**
    * Método onCreate da classe {@link HelpActivity} para a atividade activity_main.xml
    *
    * @since 17/06/2015
    * @param savedInstanceState - savedInstanceState
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // Deixa a tela da atividade sempre ligada
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Criação das variáveis para definir um tipo de font-family
        Typeface adventure = Typeface.createFromAsset(getAssets(), "adventure.ttf");
        Typeface adventurehollow = Typeface.createFromAsset(getAssets(), "adventurehollow.ttf");

        // Criação das variáveis para terem acesso aos componentes gráficos
        TextView howtoplay_title = (TextView) findViewById(R.id.TextViewHowToPlay_Title);
        TextView howtoplay_story = (TextView) findViewById(R.id.TextViewHowToPlay_Story_Title);
        TextView howtoplay_objective = (TextView) findViewById(R.id.TextViewHowToPlay_Objective_Title);
        TextView howtoplay_controls = (TextView) findViewById(R.id.TextViewHowToPlay_Controls_Title);
        Button back = (Button) findViewById(R.id.ButtonBack);
        Button play_help = (Button) findViewById(R.id.ButtonPlay_Help);
        Button about = (Button) findViewById(R.id.ButtonAbout);

        // Definição das fontes
        howtoplay_title.setTypeface(adventure);
        howtoplay_story.setTypeface(adventurehollow);
        howtoplay_objective.setTypeface(adventurehollow);
        howtoplay_controls.setTypeface(adventurehollow);
        back.setTypeface(adventurehollow);
        play_help.setTypeface(adventure);
        about.setTypeface(adventurehollow);

        // Música do Menu Ajuda
        helpmusic = MediaPlayer.create(this, R.raw.help);
        helpmusic.setLooping(true);
    }

    /**
    * Método onClickBack da classe {@link HelpActivity} para o botão "Voltar" da atividade activity_help.xml
    *
    * @since 17/06/2015
    * @param view - View
    */
    public void OnClickBack(View view){
        // Para a música do Menu Ajuda
        helpmusic.stop();

        // Finaliza a atividade activity_help.xml
        this.finish();
    }

    /**
    * Método onClickPlay_Help da classe {@link HelpActivity} para o botão "Jogar" da atividade activity_help.xml
    *
    * @since 17/06/2015
    * @param view - View
    */
    public void OnClickPlay_Help(View view){
        // Para a música do Menu Ajuda
        helpmusic.stop();

        // Finaliza a atividade activity_help.xml
        this.finish();

        // Inicia a atividade activity_gameplay.xml
        Intent intentPlay = new Intent(this, GamePlayActivity.class);
        startActivity(intentPlay);
    }

    /**
    * Método onClickAbout da classe {@link HelpActivity} para o botão "Sobre" da atividade activity_help.xml
    *
    * @since 17/06/2015
    * @param view - View
    */
    public void OnClickAbout(View view){
        // Exibe um AlertDialog para o usuário, contendo informações do projeto
        AlertDialog alertDialog = new AlertDialog.Builder(HelpActivity.this).create();
        alertDialog.setTitle(getText(R.string.button_about).toString());
        alertDialog.setMessage(getText(R.string.app_name).toString().toUpperCase() + "\n\n" + getText(R.string.app_development).toString());
        alertDialog.setButton(getText(R.string.button_ok_message).toString(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Não faça nada
            }
        });
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.show();
    }

    /**
    * Método onStart da classe {@link HelpActivity} para a atividade activity_help.xml
    *
    * @since 17/06/2015
    */
    @Override
    protected void onStart() {
        super.onStart();
        // Inicia a música do Menu Ajuda
        helpmusic.start();

        // Cria uma ImageView animada para mostrar como se deve controlar o personagem
        ImageView img = (ImageView) findViewById(R.id.ImageViewGifDevice);
        img.setBackgroundResource(R.drawable.animation_gif);
        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
        frameAnimation.start();
    }

    /**
    * Método onResume da classe {@link HelpActivity} para a atividade activity_help.xml
    *
    * @since 17/06/2015
    */
    @Override
    protected void onResume() {
        super.onResume();
        // Define a posição atual da música do Menu Ajuda quando ela foi pausada anteriormente
        helpmusic.seekTo(helpmusic.getCurrentPosition());
    }

    /**
    * Método onPause da classe {@link HelpActivity} para a atividade activity_help.xml
    *
    * @since 17/06/2015
    */
    @Override
    protected void onPause() {
        super.onPause();
        // Pausa a música do Menu Ajuda
        helpmusic.pause();
    }
}
