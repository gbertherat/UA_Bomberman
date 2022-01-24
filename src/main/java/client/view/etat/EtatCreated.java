package client.view.etat;

import client.view.ViewCommand;

public class EtatCreated implements CommandEtat {
    private ViewCommand vc;

    public EtatCreated(ViewCommand vc){
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
        System.out.println("Le jeu n'est pas lancé!");
    }

    @Override
    public void restart() {
        System.out.println("Le jeu n'est pas lancé!");
    }
}
