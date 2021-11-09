package utils;

import agent.Character;

public enum ItemType {
	FIRE_UP{
		@Override
		public void applyItem(Character character) {
			character.getInfo().setBombRange(character.getInfo().getBombRange() + 1);
		}
	},
	FIRE_DOWN{
		@Override
		public void applyItem(Character character) {
			if(character.getInfo().getBombRange() > 1){
				character.getInfo().setBombRange(character.getInfo().getBombRange() - 1);
			}
		}
	},
	FIRE_SUIT{
		@Override
		public void applyItem(Character character) {
			character.getInfo().setInvincible(true);
			character.getInfo().setTurnUntilNotInvincible(10);
		}
	},
	SKULL{
		@Override
		public void applyItem(Character character) {
			character.getInfo().setSick(true);
			character.getInfo().setTurnUntilNotSick(10);
		}
	};

	abstract public void applyItem(Character character);
}