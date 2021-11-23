package view.strategy;

import view.ViewCommand;

public class EtatFinished implements CommandStrategy {
    private ViewCommand vc;

    public EtatFinished(ViewCommand vc){
        this.vc = vc;
        this.vc.setPlayButtonEnabled(false);
        this.vc.setStepButtonEnabled(false);
        this.vc.setPauseButtonEnabled(false);
        this.vc.setRestartButtonEnabled(true);
    }

    @Override
    public void play() {
        System.out.println("Le jeu est finie!");
    }

    @Override
    public void pause() {
        System.out.println("Le jeu est finie");
    }

    @Override
    public void restart() {
        vc.setEtat(new EtatCreated(vc));
    }
}