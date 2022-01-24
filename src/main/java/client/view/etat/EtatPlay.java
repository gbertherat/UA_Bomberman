package client.view.etat;

import client.view.ViewCommand;

public class EtatPlay implements CommandEtat {
    private ViewCommand vc;

    public EtatPlay(ViewCommand vc){
        this.vc = vc;
        this.vc.setPlayButtonEnabled(false);
        this.vc.setStepButtonEnabled(false);
        this.vc.setPauseButtonEnabled(true);
        this.vc.setRestartButtonEnabled(true);
    }

    @Override
    public void play() {
        System.out.println("Le jeu est déjà lancé!");
    }

    @Override
    public void pause() {
        vc.setEtat(new EtatPause(vc));
    }

    @Override
    public void restart() {
        vc.setEtat(new EtatRestart(vc));
    }
}
