package com.gdx.alpha.game;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.gdx.alpha.effects.HitParticleEffect;
import com.gdx.alpha.entitys.Bacteriophage;
import com.gdx.alpha.entitys.Microbe;
import com.gdx.alpha.entitys.ScoreCloud;

class InteractionManager {

    private GameManager gameManager;
    private AudioManager audioManager;
    //private Bacteriophage bacteriophage;
    private HitParticleEffect hitParticleEffect;
    private ScoreCloud scoreCloud;

    InteractionManager(GameManager gameManager, AudioManager audioManager) {
        this.gameManager = gameManager;
        this.audioManager = audioManager;
        //this.bacteriophage = new Bacteriophage();
        System.out.println("InteractionManager create");
    }
    //Ввад в игру бактериофага со случайными свойтвами
    void randomizeBacteriophage(Microbe microbe) {
        if (MathUtils.random(100) > 85 && microbe.getEntity().equals("v")) { //////////////////////
            int weapon_type_bacteriophage;
            if (gameManager.getLevel() < 5)
                 weapon_type_bacteriophage = MathUtils.random(0, 1); //////////////
            else
                weapon_type_bacteriophage = MathUtils.random(0, 3);

            //System.out.println("Weapon type: "+ weapon_type_bacteriophage);
            switch (weapon_type_bacteriophage) {
                case 0:
                   /* this.bacteriophage = new Bacteriophage(new Vector2(microbe.getPosition()),
                            0.0f, gameManager.bacteriophageAtlas, "h001",0);*/
                    gameManager.getBacteriophages().add(new Bacteriophage(new Vector2(microbe.getPosition()),
                            0.0f, gameManager.bacteriophageAtlas, "h001",0));
                   // System.out.println("Pos Bacteriophage + X: "+ gameManager.getEnemies().get(i).getPosition().x +" Y: " + gameManager.getEnemies().get(i).getPosition().y);
                    break;
                case 1:
                    gameManager.getBacteriophages().add(new Bacteriophage(new Vector2(microbe.getPosition()),
                            0.0f, gameManager.bacteriophage_weapon_maceAtlas, "m001",1));
                    /*this.bacteriophage = new Bacteriophage(new Vector2(microbe.getPosition()),
                            0.0f, gameManager.bacteriophage_weapon_maceAtlas, "m001",1);*/

                    //System.out.println("Pos Bacteriophage mace X: "+ gameManager.getEnemies().get(i).getPosition().x +" Y: " + gameManager.getEnemies().get(i).getPosition().y);
                    break;
                case 2:
                    gameManager.getBacteriophages().add(new Bacteriophage(new Vector2(microbe.getPosition()),
                            0.0f, gameManager.bacteriophage_weapon_stoneAtlas, "s001",2));
                   /* this.bacteriophage = new Bacteriophage(new Vector2(microbe.getPosition()),
                            0.0f, gameManager.bacteriophage_weapon_stoneAtlas, "s001",2);*/
                    //System.out.println("Pos Bacteriophage stone X: "+ gameManager.getEnemies().get(i).getPosition().x +" Y: " + gameManager.getEnemies().get(i).getPosition().y);
                    break;
                case 3:
                    gameManager.getBacteriophages().add(new Bacteriophage(new Vector2(microbe.getPosition()),
                            0.0f, gameManager.getBacteriophage_weapon_cudgelAtlas(), "000",3));
                   /* this.bacteriophage = new Bacteriophage(new Vector2(microbe.getPosition()),
                            0.0f, gameManager.getBacteriophage_weapon_cudgelAtlas(), "000",3);*/
                    //System.out.println("Pos Bacteriophage cudgel X: "+ gameManager.getEnemies().get(i).getPosition().x +" Y: " + gameManager.getEnemies().get(i).getPosition().y);
                    break;
            }
           // System.out.println("Weapon type: "+ weapon_type_bacteriophage);
            //System.out.println("Bacter array siaze interact manager: "+ gameManager.getBacteriophages().size);
            if(gameManager.getSoundOnOff())
                audioManager.getBornBacteriophageSound().play();
        }

    }
    //Создание эффекта взрыва от столкновения  с врагом
    void createParticleEffectBlow(Integer i){
        hitParticleEffect = new HitParticleEffect(new ParticleEffect(gameManager.blow), 0.5f);
        hitParticleEffect.setPositionEffect(gameManager.getEnemies().get(i).getPositionX() + gameManager.getEnemies().get(i).getBound().getBox().getWidth() / 2,
                gameManager.getEnemies().get(i).getPositionY() + gameManager.getEnemies().get(i).getBound().getBox().getHeight() / 2);
        gameManager.hitParticleEffectArray.add(hitParticleEffect);
        //playBlowEnemySound();
    }
    //создание облака очко от столкновения игрока с врагом
     void createScoreCloudToPlayer(Integer i){
        scoreCloud = new ScoreCloud(new Vector2(gameManager.getPlayer().getPositionX() + gameManager.getPlayer().getBound().getBox().getWidth() / 2,
                gameManager.getPlayer().getPositionY() + gameManager.getPlayer().getBound().getBox().getHeight() / 2),
                gameManager.fontScoreCloudRed, gameManager.getEnemies().get(i).getPrice(), false);
        gameManager.scoreCloudArray.add(scoreCloud);
    }
    //Изменение уровня жизни игрока от столкновения с врагом
    void changePlayerHealthEnemies(Integer i){
        gameManager.getPlayer().setHealth(gameManager.getPlayer().getHealth() - gameManager.getEnemies().get(i).getPrice());
    }

    //Создание эффекта взрыва от столкновения  с пулями
    void createParticleEffectBlowSmall( Integer i){
        hitParticleEffect = new HitParticleEffect(new ParticleEffect(gameManager.blow_small), 0.5f);
        hitParticleEffect.setPositionEffect(gameManager.getBullets().get(i).getPositionX() + gameManager.getBullets().get(i).getBound().getBox().getWidth() / 2,
                gameManager.getBullets().get(i).getPositionY() + gameManager.getBullets().get(i).getBound().getBox().getHeight() / 2);
        gameManager.hitParticleEffectArray.add(hitParticleEffect);
       // return hitParticleEffect;
    }

    //Создание эффекта взрыва от столкновения с оружием
    void createParticleEffectHit(Integer j){
        hitParticleEffect = new HitParticleEffect(new ParticleEffect(gameManager.hit), 0.2f);
        hitParticleEffect.setPositionEffect(gameManager.getWeapons().get(j).getPositionX(),
                gameManager.getWeapons().get(j).getPositionY() + gameManager.getWeapons().get(j).getBounds().getBox().getHeight() / 2);
        gameManager.hitParticleEffectArray.add(hitParticleEffect);

       //return hitParticleEffect;
    }
    //Создаем эффукт столкновения с яйцк
    void createParticleEffectOvum(){
        hitParticleEffect = new HitParticleEffect(new ParticleEffect(gameManager.ovum_effect), 3.0f);
        hitParticleEffect.setPositionEffect(gameManager.getOvum().getPosition().x + gameManager.getOvum().getBoundWidth(),
                gameManager.getOvum().getPosition().y + gameManager.getOvum().getBoundHeight() / 2);
        gameManager.hitParticleEffectArray.add(hitParticleEffect);
        gameManager.setOvumEffectStart(true);
    }
    //Создаем эффект столкновения с бонусным предметом
    void createParticleEffectBonus(int i){
        hitParticleEffect = new HitParticleEffect(new ParticleEffect(gameManager.bonusEffect), 0.5f);
        hitParticleEffect.setPositionEffect(gameManager.getBonusItemsArray().get(i).getPosition().x + gameManager.getBonusItemsArray().get(i).getBonusItemsBound().getBox().getWidth(),
                gameManager.getBonusItemsArray().get(i).getPosition().y + gameManager.getBonusItemsArray().get(i).getBonusItemsBound().getBox().getWidth() / 2);
        gameManager.hitParticleEffectArray.add(hitParticleEffect);
        //gameManager.setOvumEffectStart(true);
    }

    //Изменение уровня жизни игрока от столкновения с пулями
    void changePlayerHealthBullets(Integer i){
        gameManager.getPlayer().setHealth(gameManager.getPlayer().getHealth() - gameManager.getBullets().get(i).getPrice());
    }
    //уменьшаем здоровье врага на величину силы оружия
    void changeEnemiesHealth(Integer i, Integer j){
        gameManager.getEnemies().get(i).changeHealth(gameManager.getWeapons().get(j).getHealth());
    }
    //Изменение счетчика очков на величину вознаграждения за поражение врага
    void changeScoreAmountUIEnemiesKill(Integer i){
        gameManager.setScoresAmount(gameManager.getScoresAmount()+ gameManager.getEnemies().get(i).getPrice());
        gameManager.updateScoresAmount();
    }
    //Изменение счетчика очков на величину вознаграждения за поражение пули
    void changeScoreAmountUIBulletsKill(Integer i){
        gameManager.setScoresAmount(gameManager.getScoresAmount()+ gameManager.getBullets().get(i).getPrice());
        gameManager.updateScoresAmount();
    }

    //Создание облака очков от столкновения игрока с пулями
   void createScoreCloudToPlayerBullets(Integer i){
        scoreCloud = new ScoreCloud(new Vector2(gameManager.getPlayer().getPositionX() + gameManager.getPlayer().getBound().getBox().getWidth() / 2,
            gameManager.getPlayer().getPositionY() + gameManager.getPlayer().getBound().getBox().getHeight() / 2),
        gameManager.fontScoreCloudRed, gameManager.getBullets().get(i).getPrice(), false);
        gameManager.scoreCloudArray.add(scoreCloud);
    }
    //Создание облака очков от столкновения врагов с оружием
    void createScoreCloudToEnemies(Integer i){
        scoreCloud = new ScoreCloud(new Vector2(gameManager.getEnemies().get(i).getPositionX() + gameManager.getEnemies().get(i).getBound().getBox().getWidth() / 2,
                gameManager.getEnemies().get(i).getPositionY() + gameManager.getEnemies().get(i).getBound().getBox().getHeight() / 2),
                gameManager.fontScoreCloudGreen, gameManager.getEnemies().get(i).getPrice(), true);
        gameManager.scoreCloudArray.add(scoreCloud);
    }
    //Создание облака очков для пуль
    void createScoreCloudToBullets(Integer j){
        scoreCloud = new ScoreCloud(new Vector2(gameManager.getBullets().get(j).getPositionX() + gameManager.getBullets().get(j).getBound().getBox().getWidth()/2,
                gameManager.getBullets().get(j).getPositionY() + gameManager.getBullets().get(j).getBound().getBox().getHeight()/2),
                gameManager.fontScoreCloudGreen, gameManager.getBullets().get(j).getPrice(),true);
        gameManager.scoreCloudArray.add(scoreCloud);
    }
    //Создание облака очков для сперм
    void createScoreCloudToSperms(Integer i){
        scoreCloud = new ScoreCloud(new Vector2(gameManager.getSperms().get(i).getPositionX() + gameManager.getSperms().get(i).getBound().getBox().getWidth()/2,
                gameManager.getSperms().get(i).getPositionY() + gameManager.getSperms().get(i).getBound().getBox().getHeight()/2),
                gameManager.fontScoreCloudRed, 1,false);
        gameManager.scoreCloudArray.add(scoreCloud);
    }
    //Проигрывание звука уничтожениея врага
    void playBlowEnemySound (int i){
        if(gameManager.getSoundOnOff()) {
            if (gameManager.getEnemies().get(i).getEntity().equals("v"))
                audioManager.getBlowEnemySound().play();

            if (gameManager.getEnemies().get(i).getEntity().equals("b"))
                audioManager.getBacteriumDeathSound().play();

            if (gameManager.getEnemies().get(i).getEntity().equals("c"))
                audioManager.getBlowCondomSound().play();
        }
    }

    //Подбор бектериофага
    void useBacteriophage(int i){
        //int type = gameManager.getBacteriophages().get(i).getType();
        switch (gameManager.getBacteriophages().get(i).getType()){
            case 0:
                gameManager.getPlayer().setHealth(gameManager.getPlayer().getHealth() + gameManager.getBacteriophages().get(i).getHealth());
                if (gameManager.getPlayer().getHealth() > 300)
                    gameManager.getPlayer().setHealth(300);
                if(gameManager.getSoundOnOff())
                    audioManager.getBacteriophageCatchSound().play();
                break;
            case 1:
                gameManager.getThrowWeapon().changeType(1);
                if(gameManager.getSoundOnOff())
                    audioManager.getBacteriophageCatchSound().play();
                break;
            case 2:
                gameManager.getThrowWeapon().changeType(2);
                if(gameManager.getSoundOnOff())
                    audioManager.getBacteriophageCatchSound().play();
                break;
            case 3:
                gameManager.getThrowWeapon().changeType(3);
                if(gameManager.getSoundOnOff())
                    audioManager.getBacteriophageCatchSound().play();
                break;
        }
        //System.out.println("UseBacter array size interact manager: "+ gameManager.getBacteriophages().size);
    }

  /*  public void changePlayerLifeCount(){
        if (gameManager.player.getLifeCount() < 3)
            gameManager.player.setLifeCount(gameManager.player.getLifeCount() + 1);
    }*/
    //Использование бонусных предметов
    void usingBonusItems(int i){
        //Microbe itemRemoved;
        switch (gameManager.getBonusItemsArray().get(i).getType()){
            case 0: //Череп уничтожение всех врагов и их пули в игре в данный момент

                for (int j = 0; j < gameManager.getEnemies().size; j++){
                    //System.out.println("index: " + j +" obj " +  gameManager.getEnemies().get(j) + " size: " + gameManager.getEnemies().size);
                    //Создаем еффект взрыва
                   createParticleEffectBlow(j);
                    //Изменяем счетчик очков на велечину вознаграждения за поражение врага
                    changeScoreAmountUIEnemiesKill(j);
                    //Выводим облако очков поражения врага
                   createScoreCloudToEnemies(j);

                  if(gameManager.getSoundOnOff())
                       audioManager.getBonusSkullSound().play();

                   gameManager.getEnemies().get(j).remove();
                    //System.out.println("Collision player bacter array size: " + gameManager.getBacteriophages().size);
                    //gameManager.getEnemies().removeIndex(j);
                }
                gameManager.getEnemies().clear();

                for (int b = 0; b < gameManager.getBullets().size; b++){
                    createParticleEffectBlowSmall(b);
                    changeScoreAmountUIBulletsKill(b);
                    createScoreCloudToBullets(b);
                    gameManager.getBullets().get(b).remove();
                }
                gameManager.getBullets().clear();
                break;
            case 1://Монета прибавление 5000 очков
                gameManager.setScoresAmount(gameManager.getScoresAmount() + 5000);
                if (gameManager.getSoundOnOff())
                    audioManager.getBonusCoinSound().play();
                break;
            case 2:// Будилник вирусы перестают стрелять и начинают двигаться в противоположном направлении

                for (int j = 0; j < gameManager.getEnemies().size; j++){
                    if (gameManager.getEnemies().get(j).getEntity().equals("v")) {
                        gameManager.getEnemies().get(j).setSpeed(gameManager.getEnemies().get(j).getSpeed() / -2.0f);
                        gameManager.getEnemies().get(j).setInterval(0.0f);
                        gameManager.getEnemies().get(j).setPrice(gameManager.getEnemies().get(j).getPrice() * 2);
                    }
                }
                if(gameManager.getSoundOnOff())
                    audioManager.getAlarmClockSound().play();
                break;
            case 3:
                if (gameManager.getPlayer().getLifeCount() < 6)
                    gameManager.getPlayer().setLifeCount(gameManager.getPlayer().getLifeCount() + 1);
                if(gameManager.getSoundOnOff())
                    audioManager.getNewLifeSound().play();
                break;
        }
    }
    //воспроизведение звука гибели бактериофага
    void playBacteriophageDeathSound(){
        if(gameManager.getSoundOnOff())
            audioManager.getBacteriophageDeathSound().play();
    }
    //Воспроизведение звука гибели сперм
    void playHitSpermSound(){
        if(gameManager.getSoundOnOff())
            audioManager.getHitSpermSound().play();
    }
}//end of class
