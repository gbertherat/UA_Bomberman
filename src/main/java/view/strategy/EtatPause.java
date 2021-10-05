package view.strategy;

import view.ViewCommand;

public class EtatPause implements CommandStrategy{
    private ViewCommand vc;

    public EtatPause(ViewCommand vc){
        this.vc = vc;
        this.vc.setPlayButtonEnabled(true);
        this.vc.setStepButtonEnabled(true);
        this.vc.setPauseButtonEnabled(false);
        this.vc.setRestartButtonEnabled(true);
    }

    @Override
    public void play() {
        vc.setEtat(new EtatPlay(vc));
    }

    @Override
    public void pause() {
        System.out.println("Le jeu est déjà en pause!");
    }

    @Override
    public void restart() {
        vc.setEtat(new EtatRestart(vc));
    }
}
