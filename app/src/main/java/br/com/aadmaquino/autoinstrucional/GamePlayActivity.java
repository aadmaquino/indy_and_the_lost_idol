package br.com.aadmaquino.autoinstrucional;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.Random;
import static br.com.aadmaquino.autoinstrucional.AnimationView.detectCollisionBall;
import static br.com.aadmaquino.autoinstrucional.AnimationView.detectCollisionVictory;
import static br.com.aadmaquino.autoinstrucional.AnimationView.detectCollisionWall;

/**
* Classe GamePlayActivity para a atividade activity_gameplay.xml
*
* @author Antônio Augusto Duarte Moura de Aquino
* @author Iago Ávila Duarte
* @version 1.0
* @since 17/06/2015
*/
public class GamePlayActivity extends Activity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private static final String TAG = "br.com.aadmaquino.autoinstrucional.AnimationView";
    private PowerManager.WakeLock mWakeLock;
    private TextView actdescription;
    private View animationView;
    private MediaPlayer actmusic;
    private MediaPlayer deathmusic;
    private MediaPlayer victorymusic;
    private long lastUpdate;
    public static float lastX = 0;
    public static float lastY = 0;

    /**
    * Método randInt para a atividade activity_gameplay.xml
    *
    * @param min - Valor Mínimo inteiro.
    * @param max - Valor Máximo inteiro.
    * @return Retorna o número aleatório gerado.
    */
    public static int randInt(int min, int max) {
        // Gera um número aleatório num determinado intervalo
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    /**
    * Método onCreate da classe {@link GamePlayActivity} para a atividade activity_gameplay.xml
    *
    * @since 17/06/2015
    * @param savedInstanceState - savedInstanceState
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);

        // Definição do Sensor a ser utilizado. No caso, o Acelerômetro
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        lastUpdate = System.currentTimeMillis();

        // Deixa a tela da atividade sempre ligada
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        PowerManager mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, TAG);

        // Criação das variáveis para terem acesso aos componentes gráficos
        actdescription = (TextView) findViewById(R.id.TextViewActDescription);
        animationView = findViewById(R.id.ViewGamePlay);

        // Escolher aleatoriamente músicas de ação
        int act_random = randInt(1, 3);
        int act_aux;
        if(act_random == 1) {
            act_aux = R.raw.act1;
        } else if(act_random == 2) {
            act_aux = R.raw.act2;
        } else {
            act_aux = R.raw.act3;
        }

        // Escolher aleatoriamente músicas de derrota
        int death_random = randInt(1, 2);
        int death_aux;
        if(death_random == 1) {
            death_aux = R.raw.death1;
        } else {
            death_aux = R.raw.death2;
        }

        // Música de ação
        actmusic = MediaPlayer.create(this, act_aux);
        actmusic.setLooping(true);

        // Música de derrota
        deathmusic = MediaPlayer.create(this, death_aux);
        deathmusic.setLooping(false);

        // Música de vitória
        victorymusic = MediaPlayer.create(this, R.raw.victory);
        victorymusic.setLooping(false);
    }

    /**
    * Método onStart da classe {@link GamePlayActivity} para a atividade activity_gameplay.xml
    *
    * @since 17/06/2015
    */
    @Override
    protected void onStart() {
        super.onStart();
        // Variáveis de detecção de colisão
        detectCollisionWall = false;
        detectCollisionBall = false;
        detectCollisionVictory = false;

        // Mostra um texto indicando que o jogo ainda não está em execução
        actdescription.setText(R.string.textview_start_description);

        // Inicia a música de ação
        actmusic.start();

        // Deixa a thread pausada, para que o jogo inicie assim que o usuário apertar na tela
        AnimationView.onPause();

        // Se o usuário tocou na tela, então o jogo vai começar
        animationView.setOnTouchListener(
                new RelativeLayout.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent m) {
                        // Retira o texto de pausa
                        actdescription.setText("");
                        // Resume a thread que estava pausada anteriormente
                        AnimationView.onResume();
                        return true;
                    }
                }
        );

        // Thread para mostrar uma mensagem indicando se o jogador perdeu ou venceu a partida.
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1);
                        if (detectCollisionWall) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    AnimationView.onPause();
                                    detectCollisionWall = false;
                                    msgIndianaWall();
                                }
                            });
                            break;
                        } else if (detectCollisionBall) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    AnimationView.onPause();
                                    detectCollisionBall = false;
                                    msgIndianaBall();
                                }
                            });
                            break;
                        } else if (detectCollisionVictory) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    AnimationView.onPause();
                                    detectCollisionVictory = false;
                                    msgIndianaWins();
                                }
                            });
                            break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
    * Método msgIndianaWall da classe {@link GamePlayActivity} para a atividade activity_gameplay.xml
    *
    * @since 17/06/2015
    */
    protected void msgIndianaWall() {
        // Para a música de ação
        actmusic.stop();

        // Inicia a música de derrota
        deathmusic.start();

        // Exibe uma mensagem informando que o usuário perdeu a partida, porque Indiana Jones
        // encostou na parede
        AlertDialog.Builder builder = new AlertDialog.Builder(GamePlayActivity.this);
        builder.setTitle(R.string.action_lose);
        builder.setMessage(R.string.action_lose_message1);
        builder.setPositiveButton(R.string.button_try_again, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Se o usuário optar por tentar de novo, todas as músicas vão ser paradas
                actmusic.stop();
                victorymusic.stop();
                deathmusic.stop();

                // E então, a atividade será reiniciada
                Intent i = new Intent(GamePlayActivity.this, GamePlayActivity.class);
                GamePlayActivity.this.startActivity(i);
                GamePlayActivity.this.finish();
            }
        });
        builder.setNegativeButton(R.string.button_exit_game, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Ou se o usuário optar por sair do jogo, todas as músicas vão ser paradas também
                actmusic.stop();
                victorymusic.stop();
                deathmusic.stop();

                // A atividade será finalizada e voltará para a atividade já ativa, a MainActivity.
                GamePlayActivity.this.finish();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    /**
    * Método msgIndianaBall da classe {@link GamePlayActivity} para a atividade activity_gameplay.xml
    *
    * @since 17/06/2015
    */
    protected void msgIndianaBall() {
        // Para a música de ação
        actmusic.stop();

        // Inicia a música de derrota
        deathmusic.start();

        // Exibe uma mensagem informando que o usuário perdeu a partida, porque Indiana Jones
        // foi pego pelo índio
        AlertDialog.Builder builder = new AlertDialog.Builder(GamePlayActivity.this);
        builder.setTitle(R.string.action_lose);
        builder.setMessage(R.string.action_lose_message2);
        builder.setPositiveButton(R.string.button_try_again, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Se o usuário optar por tentar de novo, todas as músicas vão ser paradas
                actmusic.stop();
                victorymusic.stop();
                deathmusic.stop();

                // E então, a atividade será reiniciada
                Intent i = new Intent(GamePlayActivity.this, GamePlayActivity.class);
                GamePlayActivity.this.startActivity(i);
                GamePlayActivity.this.finish();
            }
        });
        builder.setNegativeButton(R.string.button_exit_game, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Ou se o usuário optar por sair do jogo, todas as músicas vão ser paradas também
                actmusic.stop();
                victorymusic.stop();
                deathmusic.stop();

                // A atividade será finalizada e voltará para a atividade já ativa, a MainActivity.
                GamePlayActivity.this.finish();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    /**
    * Método msgIndianaWins da classe {@link GamePlayActivity} para a atividade activity_gameplay.xml
    *
    * @since 17/06/2015
    */
    protected void msgIndianaWins() {
        // Para a música de ação
        actmusic.stop();

        // Inicia a música de vitória
        victorymusic.start();

        // Exibe uma mensagem informando que o usuário venceu a partida, porque Indiana Jones
        // alcançou o ídolo dourado
        AlertDialog.Builder builder = new AlertDialog.Builder(GamePlayActivity.this);
        builder.setTitle(R.string.action_win);
        builder.setMessage(R.string.action_win_message);
        builder.setPositiveButton(R.string.button_play_again, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Se o usuário optar por jogar de novo, todas as músicas vão ser paradas
                actmusic.stop();
                victorymusic.stop();
                deathmusic.stop();

                // E então, a atividade será reiniciada
                Intent i = new Intent(GamePlayActivity.this, GamePlayActivity.class);
                GamePlayActivity.this.startActivity(i);
                GamePlayActivity.this.finish();
            }
        });
        builder.setNegativeButton(R.string.button_exit_game, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Ou se o usuário optar por sair do jogo, todas as músicas vão ser paradas também
                actmusic.stop();
                victorymusic.stop();
                deathmusic.stop();

                // A atividade será finalizada e voltará para a atividade já ativa, a MainActivity.
                GamePlayActivity.this.finish();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    /**
    * Método onResume da classe {@link GamePlayActivity} para a atividade activity_gameplay.xml
    *
    * @since 17/06/2015
    */
    @Override
    protected void onResume() {
        super.onResume();
        // Força o "não-desligamento" da tela
        mWakeLock.acquire();

        // Registra todos os valores do Acelerômetro
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        // Define a posição atual das músicas quando elas foram pausadas anteriormente
        actmusic.seekTo(actmusic.getCurrentPosition());
        deathmusic.seekTo(deathmusic.getCurrentPosition());
        victorymusic.seekTo(victorymusic.getCurrentPosition());
    }

    /**
    * Método onPause da classe {@link GamePlayActivity} para a atividade activity_gameplay.xml
    *
    * @since 17/06/2015
    */
    @Override
    protected void onPause() {
        super.onPause();
        // Libera a tela para que ela possa apagar
        mWakeLock.release();

        // Deixa de utilizar o Acelerômetro
        sensorManager.unregisterListener(this);

        // Pausa todas as músicas
        actmusic.pause();
        deathmusic.pause();
        victorymusic.pause();

        // Pausa a thread de redesenho
        AnimationView.onPause();
    }

    /**
    * Método onBackPressed da classe {@link GamePlayActivity} para a atividade activity_gameplay.xml
    *
    * @since 17/06/2015
    */
    @Override
    public void onBackPressed() {
        // Se o usuário apertar a tecla "Voltar" do dispositivo, vão acontecer os seguintes eventos:
        // * Pausa da thread de redesenho
        AnimationView.onPause();

        // * Exibição de um texto indicando que o jogo não está em execução
        actdescription.setText(R.string.textview_start_description);

        // * Chamada do método doExit
        doExit();
    }

    /**
    * Método onExit da classe {@link GamePlayActivity} para a atividade activity_gameplay.xml
    *
    * @since 17/06/2015
    */
    protected void doExit() {
        // Exibe um AlertDialog perguntando se o usuário deseja sair do jogo
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.action_exit_gameplay);
        builder.setMessage(R.string.action_exitgame_message);
        builder.setPositiveButton(R.string.button_yes_message, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Se o usuário optar por sair do jogo, todas as músicas vão ser paradas,
                actmusic.stop();
                victorymusic.stop();
                deathmusic.stop();

                // Todas as variáveis de detecção de colisão serão setadas para true, para que
                // o while consiga sair da Thread que verifica e exibe uma mensagem indicando se
                // o usuário venceu a partida ou não.
                detectCollisionWall = true;
                detectCollisionBall = true;
                detectCollisionVictory = true;

                // A atividade será finalizada e voltará para a atividade já ativa, a MainActivity.
                GamePlayActivity.this.finish();
            }
        });
        builder.setNegativeButton(R.string.button_no_message, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Se não, o método onStart será chamado
                onStart();
            }
        });
        builder.show();
    }

    /**
    * Método onSensorChanged da classe {@link GamePlayActivity} para a atividade activity_gameplay.xml
    *
    * @param sensorEvent - Evento do Sensor
    */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // Verifica se o sensor utilizado é realmente o Acelerômetro
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // Intervalo de leitura das informações do Acelerômetro
            long actualTime = System.currentTimeMillis();
            if (actualTime + lastUpdate > 500) {
                // Leitura das informações do eixo X do Acelerômetro
                float x = sensorEvent.values[0];
                // Leitura das informações do eixo Y do Acelerômetro
                float y = sensorEvent.values[1];

                // Armazena o valor do eixo X do Acelerômetro
                lastX = -x;
                // Armazena o valor do eixo Y do Acelerômetro
                lastY = y;
            }
        }
    }

    /**
    * Método onAccuracyChanged da classe {@link GamePlayActivity} para a atividade activity_gameplay.xml
    *
    * @param sensor - sensor
    * @param i - i
    */
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // não é utlizada nesse projeto.
    }
}
