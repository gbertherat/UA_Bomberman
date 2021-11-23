package utils;

import agent.Character;

public enum ItemType {
	FIRE_UP{
		@Override
		public void applyItem(Character character) {
			character.getInfo().increaseBombRangeBy(1);
		}

		@Override
		public boolean isGood() {
			return true;
		}
	},
	FIRE_DOWN{
		@Override
		public void applyItem(Character character) {
			if(character.getInfo().getBombRange() > 1){
				character.getInfo().decreaseBombRange(1);
			}
		}

		@Override
		public boolean isGood() {
			return false;
		}
	},
	FIRE_SUIT{
		@Override
		public void applyItem(Character character) {
			character.getInfo().setInvincible(true);
			character.getInfo().setSick(false);
			character.getInfo().setTurnUntilNotSick(0);
			character.getInfo().setTurnUntilNotInvincible(10);
		}

		@Override
		public boolean isGood() {
			return true;
		}
	},
	SKULL{
		@Override
		public void applyItem(Character character) {
			if(!character.getInfo().isInvincible()) {
				character.getInfo().setSick(true);
				character.getInfo().setTurnUntilNotSick(10);
			}
		}

		@Override
		public boolean isGood() {
			return false;
		}
	};

	abstract public void applyItem(Character character);
	abstract public boolean isGood();
}