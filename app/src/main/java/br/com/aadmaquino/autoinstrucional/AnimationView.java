package br.com.aadmaquino.autoinstrucional;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import java.util.Random;
import static br.com.aadmaquino.autoinstrucional.GamePlayActivity.lastX;
import static br.com.aadmaquino.autoinstrucional.GamePlayActivity.lastY;

/**
* Classe AnimationView
*
* @author Antônio Augusto Duarte Moura de Aquino
* @author Iago Ávila Duarte
* @version 1.0
* @since 17/06/2015
*/
public class AnimationView extends View {
    private static final int BALL_SIZE = 100;
    private static final float BALL_MAX_VELOCITY = 80;
    private Bitmap alvo_bgimage;
    private Sprite alvo_move;
    private Bitmap heroi_bgimage;
    private Sprite heroi_move;
    private Bitmap inimigo_bgimage;
    private Sprite inimigo_move;
    public static DrawingThread dthread;
    public static boolean detectCollisionBall = false;
    public static boolean detectCollisionWall = false;
    public static boolean detectCollisionVictory = false;

    /**
    * Método randInt da classe {@link AnimationView}
    *
    * @since 17/06/2015
    * @param min - Valor Mínimo inteiro.
    * @param max - Valor Máximo inteiro.
    * @return Retorna o número aleatório gerado.
    */
    public static int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    /**
    * Método construtor da classe {@link AnimationView}
    *
    * @since 17/06/2015
    * @param context - Objeto da classe {@link Context}
    * @param attrs - Objeto da classe {@link AttributeSet}
    */
    public AnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // As 6 linhas abaixo pegam todas as informações relacionadas ao tamanho da tela do
        // dispositivo e as armazenam dentro de 2 variáveis inteiras, para que o centro da largura
        // da tela e a altura máxima sejam encontrados.
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;


        // Bitmap para o alvo (Ídolo Dourado)
        Bitmap alvo_bit = BitmapFactory.decodeResource(getResources(), R.drawable.idol);
        // Define o tamanho do Bitmap do alvo
        alvo_bgimage = Bitmap.createScaledBitmap(alvo_bit, BALL_SIZE, BALL_SIZE, true);
        // Sprite alvo auxiliar para o Bitmap do alvo que será utilizado como referência de posicionamento na View
        alvo_move = new Sprite();
        // Define a localização exata do alvo, com o centro da largura calculado
        alvo_move.setLocation((width - BALL_SIZE) / 2, 0);
        // Define o tamanho do alvo
        alvo_move.setSize(BALL_SIZE, BALL_SIZE);
        // Define o cor do alvo (sem necessidade, mas é apenas para testes de posicionamento)
        alvo_move.paint.setARGB(0, 0, 255, 0);


        // Bitmap para o herói (Indiana Jones)
        Bitmap heroi_bit = BitmapFactory.decodeResource(getResources(), R.drawable.balljones);
        // Define o tamanho do Bitmap do herói
        heroi_bgimage = Bitmap.createScaledBitmap(heroi_bit, BALL_SIZE, BALL_SIZE, true);
        // Sprite auxiliar para o Bitmap do herói que será utilizado como referência de posicionamento na View
        heroi_move = new Sprite();
        // Define a localização exata do herói, com o centro da largura e a altura máxima calculados
        heroi_move.setLocation((width - BALL_SIZE) / 2, height - BALL_SIZE - 98);//**** - 98 para que o usuário não toque na parede sempre que comece o jogo
        // Define o tamanho do herói
        heroi_move.setSize(BALL_SIZE, BALL_SIZE);
        // Define o cor do herói (sem necessidade, mas é apenas para testes de posicionamento)
        heroi_move.paint.setARGB(0, 0, 0, 255);


        // Bitmap para o inimigo (Índio da Tribo Jarawara)
        Bitmap inimigo_bit = BitmapFactory.decodeResource(getResources(), R.drawable.ballindian);
        // Define o tamanho do Bitmap do inimigo
        inimigo_bgimage = Bitmap.createScaledBitmap(inimigo_bit, BALL_SIZE, BALL_SIZE, true);
        // Sprite auxiliar para o Bitmap do inimigo que será utilizado como referência de posicionamento na View
        inimigo_move = new Sprite();
        // Define uma localização aleatória do inimigo, num intervalo entre 0 e os tamanhos da Largura e Altura da tela
        inimigo_move.setLocation(randInt(0, width - BALL_SIZE - 1), randInt(0, height - BALL_SIZE - 58));
        // Define o tamanho do inimigo
        inimigo_move.setSize(BALL_SIZE, BALL_SIZE);
        // Define uma velocidade aleatória do inimigo ao se mover
        inimigo_move.setVelocity(
                (float) ((Math.random() - .5) * 2 * BALL_MAX_VELOCITY),
                (float) ((Math.random() - .5) * 2 * BALL_MAX_VELOCITY)
        );
        // Define o cor do inimigo (sem necessidade, mas é apenas para testes de posicionamento)
        inimigo_move.paint.setARGB(0, 255, 0, 0);

        // Inicia a Thread da classe DrawingThread para redesenhar a tela numa taxa de 60 quadros por segundo (FPS)
        dthread = new DrawingThread(this, 60);
        dthread.start();
    }

    /**
    * Método onPause da classe {@link AnimationView}
    *
    * @since 17/06/2015
    */
    public static void onPause() {
        // Pausa a thread de redesenho
        dthread.stop();
    }

    /**
    * Método onResume da classe {@link AnimationView}
    *
    * @since 17/06/2015
    */
    public static void onResume() {
        // Retoma a thread de redesenho
        dthread.start();
    }

    /**
    * Método onDraw da classe {@link AnimationView}
    *
    * @since 17/06/2015
    * @param canvas - Objeto da classe {@link Canvas}
    */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Desenha o Sprite alvo
        canvas.drawOval(alvo_move.rect, alvo_move.paint);
        // Desenha o Bitmap alvo
        canvas.drawBitmap(alvo_bgimage, alvo_move.rect.left, alvo_move.rect.top, null);

        // Desenha o Sprite herói
        canvas.drawOval(heroi_move.rect, heroi_move.paint);
        // Desenha o Bitmap herói
        canvas.drawBitmap(heroi_bgimage, heroi_move.rect.left, heroi_move.rect.top, null);

        // Desenha o Sprite inimigo
        canvas.drawOval(inimigo_move.rect, inimigo_move.paint);
        // Desenha o Bitmap inimigo
        canvas.drawBitmap(inimigo_bgimage, inimigo_move.rect.left, inimigo_move.rect.top, null);

        // Atualiza o posicionamento dos Sprites
        updateSprites();

        // Verifica se houve alguma colisão específica
        updateCollision();

        // Atualiza o posicionamento do Sprite do herói
        updatePositionSensor();
    }

    /**
    * Método updateSprites da classe {@link AnimationView}
    *
    * @since 17/06/2015
    */
    private void updateSprites() {
        // Atualização do posicionamento dos 3 sprites
        alvo_move.move();
        inimigo_move.move();
        heroi_move.move();

        // Verifica se o inimigo bateu no lado esquerdo ou direito da tela
        if (inimigo_move.rect.left < 0 || inimigo_move.rect.right >= getWidth()) {
            // Inverte o valor do eixo X
            inimigo_move.dx = -inimigo_move.dx;
        }

        // Verifica se o inimigo bateu no "cabeçalho" (top) ou no "rodapé" (bottom) da tela
        if (inimigo_move.rect.top < 0 || inimigo_move.rect.bottom >= getHeight()) {
            // Inverte o valor do eixo Y
            inimigo_move.dy = -inimigo_move.dy;
        }
    }

    /**
    * Método updateSprites da classe {@link AnimationView}
    *
    * @since 17/06/2015
    */
    public void updateCollision() {
        // Verifica se o herói tocou nas laterais da tela
        if (heroi_move.rect.left < 0 || heroi_move.rect.right >= getWidth() || heroi_move.rect.top < 0 || heroi_move.rect.bottom >= getHeight()) {
            // Define o valor do boolean detectCollisionWall para true
            setDetectCollisionWall(true);
            // Pausa a thread de redesenho
            dthread.stop();
        }

        // Verifica se o inimigo pegou o herói
        if (inimigo_move.rect.intersects(heroi_move.rect.left, heroi_move.rect.top, heroi_move.rect.right, heroi_move.rect.bottom)) {
            // Define o valor do boolean detectCollisionBall para true
            setDetectCollisionBall(true);
            // Pausa a thread de redesenho
            dthread.stop();
        }

        // Verifica se o alvo foi alcançado pelo herói
        if (heroi_move.rect.intersects(alvo_move.rect.left, alvo_move.rect.top, alvo_move.rect.right, alvo_move.rect.bottom)) {
            // Define o valor do boolean detectCollisionVictory para true
            setDetectCollisionVictory(true);
            // Pausa a thread de redesenho
            dthread.stop();
        }
    }

    /**
    * Método updateSprites da classe {@link AnimationView}
    *
    * @since 17/06/2015
    */
    public void updatePositionSensor() {
        // Atualização do posicionamento do sprite herói
        heroi_move.move();

        // Definição da velocidade do herói a partir dos valores do Acelerômetro
        heroi_move.setVelocity(lastX, lastY);

        // Definição do posicionamento do herói em relação aos eixos X e Y a partir dos valores do Acelerômetro
        heroi_move.dx += lastX;
        heroi_move.dy += lastY;
    }

    /**
    * Método setDetectCollisionWall da classe {@link AnimationView}
    *
    * @param b - boolean
    * @return detectCollisionWall - Retorna o valor do detectCollisionWall
    */
    public static boolean setDetectCollisionWall(boolean b) {
        detectCollisionWall = b;
        return detectCollisionWall;
    }

    /**
    * Método setDetectCollisionBall da classe {@link AnimationView}
    *
    * @param b - boolean
    * @return detectCollisionBall - Retorna o valor do detectCollisionBall
    */
    public static boolean setDetectCollisionBall(boolean b) {
        detectCollisionBall = b;
        return detectCollisionBall;
    }

    /**
    * Método setDetectCollisionVictory da classe {@link AnimationView}
    *
    * @param b - boolean
    * @return detectCollisionVictory - Retorna o valor do detectCollisionVictory
    */
    public static boolean setDetectCollisionVictory(boolean b) {
        detectCollisionVictory = b;
        return detectCollisionVictory;
    }
}