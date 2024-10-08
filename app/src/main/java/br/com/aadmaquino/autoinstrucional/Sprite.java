package br.com.aadmaquino.autoinstrucional;

import android.graphics.Paint;
import android.graphics.RectF;

/**
* Classe Sprite
*
* @author Antônio Augusto Duarte Moura de Aquino
* @author Iago Ávila Duarte
* @version 1.0
* @since 17/06/2015
*/
public class Sprite {
    public RectF rect = new RectF();
    public float dx = 0;
    public float dy = 0;
    public Paint paint = new Paint();

    /**
    * Superclass da classe {@link Sprite} (pacote: dados)
    *
    * @since 28/04/2015
    */
    public Sprite() {
       super();
    }

    /**
    * Método construtor da classe {@link Sprite}
    *
    * @since 17/06/2015
    * @param x - Localização do Sprite em relação ao eixo X.
    * @param y - Localização do Sprite em relação ao eixo Y.
    * @param width - Largura do Sprite.
    * @param height - Altura do Sprite.
    */
    public Sprite(float x, float y, float width, float height) {
        setLocation(x, y);
        setSize(width, height);
    }

    /**
    * Método move da classe {@link Sprite}
    *
    * @since 17/06/2015
    */
    public void move() {
        // Define a velocidade de locomoção do Sprite em relação aos eixos X (dx) e Y (dy).
        rect.offset(dx, dy);
    }

    /**
    * Método stopMoving da classe {@link Sprite}
    *
    * @since 17/06/2015
    */
    public void stopMoving() {
        // Para o movimento do Sprite e define a velocidade para (0, 0).
        setVelocity(0, 0);
    }

    /**
    * Método setLocation da classe {@link Sprite}
    *
    * @since 17/06/2015
    * @param x - Localização do Sprite em relação ao eixo X.
    * @param y - Localização do Sprite em relação ao eixo Y.
    */
    public void setLocation(float x, float y) {
        // Define a localização do Sprite na tela com os valores x e y informados.
        rect.offsetTo(x, y);
    }

    /**
    * Método setSize da classe {@link Sprite}
    *
    * @since 17/06/2015
    * @param width - Largura do Sprite.
    * @param height - Altura do Sprite.
    */
    public void setSize(float width, float height) {
        // Defome a largura e altura do Sprite na tela com os valores width e height informados
        rect.right = rect.left + width;
        rect.bottom = rect.top + height;
    }

    /**
    * Método setVelocity da classe {@link Sprite}
    *
    * @since 17/06/2015
    * @param dx - Velocidade de locomoção do Sprite em relação ao eixo X.
    * @param dy - Velocidade de locomoção do Sprite em relação ao eixo Y.
    */
    public void setVelocity(float dx, float dy) {
        // Define a velocidade de locomoção do Sprite na tela com os valores dx e dy informados
        this.dx = dx;
        this.dy = dy;
    }
}
