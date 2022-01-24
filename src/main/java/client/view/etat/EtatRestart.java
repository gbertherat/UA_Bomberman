package client.view.etat;

import client.view.ViewCommand;

public class EtatRestart implements CommandEtat {
    private ViewCommand vc;

    public EtatRestart(ViewCommand vc){
        this.vc = vc;
        this.vc.setPlayButtonEnabled(true);
        this.vc.setStepButtonEnabled(false);
        this.vc.setPauseButtonEnabled(false);
        this.vc.setRestartButtonEnabled(false);
    }

    @Override
    public void play() {
        vc.setEtat(new EtatPlay(vc));
    }

    @Override
    public void pause() {
        System.out.println("Le jeu n'a pas été lancé!");
    }

    @Override
    public void restart() {
        System.out.println("Le jeu a déjà été redémarré!");
    }
}
