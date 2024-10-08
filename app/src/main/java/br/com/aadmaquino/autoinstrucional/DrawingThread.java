package br.com.aadmaquino.autoinstrucional;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

/**
* Classe DrawingThread
*
* @author Antônio Augusto Duarte Moura de Aquino
* @author Iago Ávila Duarte
* @version 1.0
* @since 17/06/2015
*/
public class DrawingThread {
    private View view = null;
    private int fps;
    private Thread thread = null;
    private Handler handler = null;
    private volatile boolean isRunning = false;

    /**
    * Método construtor da classe {@link DrawingThread}
    *
    * @since 17/06/2015
    * @param view - View
    * @param fps - Taxa de quadros por segundo
    */
    public DrawingThread(View view, int fps) {
        // Constrói um novo DrawingThread para atualizar o componente gráfico CustomView da atividade
        // activity_gameplay.xml , numa determinada taxa de quadros por segundo (FPS).
        // Esse método NÃO inicia o segmento em execução. Chame o método start() para fazê-lo .
        if (view == null || fps <= 0) {
            throw new IllegalArgumentException();
        }
        this.view = view;
        this.fps = fps;
        this.handler = new Handler(Looper.getMainLooper());
    }

    /**
    * Método isRunning da classe {@link DrawingThread}
    *
    * @since 17/06/2015
    * @return thread != null - verifica se a thread está em execução
    */
    public boolean isRunning() {
        return thread != null;
    }

    /**
    * Método start da classe {@link DrawingThread}
    *
    * @since 17/06/2015
    */
    public void start() {
        // Inicia a thread e, então, redesenha a View repetidamente
        if (thread == null) {
            thread = new Thread(new MainRunner());
            thread.start();
        }
    }

    /**
    * Método stop da classe {@link DrawingThread}
    *
    * @since 17/06/2015
    */
    public void stop() {
        // Para a thread e não redesenha mais a View
        if (thread != null) {
            isRunning = false;
            try {
                thread.join();
            } catch (InterruptedException ie) {
                // não faça nada
            }
            thread = null;
        }
    }

    /**
    * Método MainRunner da classe {@link DrawingThread}
    *
    * @since 17/06/2015
    */
    private class MainRunner implements Runnable {
        // Pequena classe auxiliar executável que contém o loop principal da thread para
        // redesenhar a View e "dormir" num determinado período repedidamente
        public void run() {
            isRunning = true;
            while (isRunning) {
                // Thread "dorme" durante um curto espaço de tempo entre os quadros de animação (FPS)
                try {
                    Thread.sleep(1000 / fps);
                } catch (InterruptedException ie) {
                    isRunning = false;
                }

                // Posta uma mensagem que vai causar o redesenho da View
                handler.post(new Updater());
            }
        }
    }

    /**
    * Método Updater da classe {@link DrawingThread}
    *
    * @since 17/06/2015
    */
    private class Updater implements Runnable {
        // Pequena classe auxiliar executável necessária pelo Android para redesenhar uma View
        // ou até mesmo forçar o redesenho dela.
        public void run() {
            view.invalidate();
        }
    }
}
