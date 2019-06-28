package com.gdx.alpha.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.gdx.alpha.effects.HitParticleEffect;
import com.gdx.alpha.entitys.BacteriasColony;
import com.gdx.alpha.entitys.Condom;
import com.gdx.alpha.entitys.Microbe;
import com.gdx.alpha.entitys.ScoreCloud;
import com.gdx.alpha.entitys.Virus;
import com.gdx.alpha.entitys.VirusBullet;
import com.gdx.alpha.entitys.Weapon;
import com.gdx.alpha.screens.GameScreen;
import com.gdx.alpha.screens.ObjectScreen;

/**
 * Created by Ro|)e|\| on 04.02.15.
 */
public class GameDriver {

    private ObjectScreen gameScreen;
    private GameManager gameManager;
    private CollisionDetector collisionDetector;
    private InteractionManager interactionManager;
    private AudioManager audioManager;
    private Actor removedActor;
    private Weapon removedAxe;
    private Float removedTime;
    private HitParticleEffect removedHPE;
    private ScoreCloud removedScoreCloud;
    private Microbe microbe;
    private BacteriasColony colony;

    private float gameTime = 0.0f;

    public GameManager getGameManager() {
        return gameManager;
    }
    public AudioManager getAudioManager(){return audioManager;}

    public GameDriver(GameScreen gameScreen, int level){
        this.gameScreen = gameScreen;
        //Создаем экземпляр класса GameManager
        gameManager = new GameManager(level);
        //Создаем AudioManager
        audioManager = new AudioManager();
        //interaction manager
        interactionManager = new InteractionManager(gameManager, audioManager);
        //Создаем экзкмпляр класса CollisionDetector и передаем в него экземпляр класса GameManager
        collisionDetector = new CollisionDetector(gameManager,interactionManager);
        //загружаем ресурсы игры (текстуры)
        gameManager.loadSources();
        //инициализируем игрока и каплю
        gameManager.buildGeneralPlayers();
        //инициализируем и стартуем процесс бросания оружия
        gameManager.buildActions();
        //инициализируем строку состояния игры
        gameManager.buildUiStateString();
        //Вкл/Выкл звук
        gameManager.setSoundOnOff(gameScreen.getScreenManager().getOnoff());
        System.out.println("GameDriver create");
    }

/*gameDrive фукция которая является игровым процессом
* функция вводит в игровой процесс всех объектов
* контролирует процессы взаимодействия объектов
* и удаление объектов из игрововго процесса*/
    public void gameDrive(float delta){
        //Блок ввода в игру объектов игрововго процесса
        addEnemiesToGame(delta);
        addBulletsToArray();
        addBulletsToGame();
        addWeaponToGame();
        addSpermsToGame();
        addHitParticleEffectToGame();
        addScoreCloudToGame();
        addBacteriophageToGame();
        addOvumToGame();
        addBacteriumToGame();
        //---------------------------------------------------

        //Блок контроля столкновений и взаимодействия объектов
        collisionDetector.detectPlayerCollisions();
        collisionDetector.detectWeaponBulletCollision();
        collisionDetector.detectWeaponEnemyCollisions();
        collisionDetector.detectSpermCollisions();
        collisionDetector.detectBacteriophageEnemyCollisions();
        collisionDetector.detectOvumSpermsCollisions();
        collisionDetector.detectLevelEnd();
        //----------------------------------------------------

        //Блок контроля нахождения объектов игрового процесса на игровой сцене
        // и удаления если объекты покинули пределы игровой сцены
        controlEnemiesPosition();
        controlBulletPosition();
        controlWeaponPosition();
        controlHitParticleEffect();
        controlScoreCloud();
        controlLifeScale();
        controlLevelEnd();
        /*Запуск задержки для отрисовки ovum_effect и выходом на экран с выбором уровня
        * */
        if(gameManager.getOvumEffectStart())
            changeDeltaTimeParticleEffect(delta);
    }
    //Функция ввода основных объектов в игру
    public void addGeneralActorsToScene(){
        gameScreen.getGameStage().addActor(gameManager.getBackgroundLayer00());
        gameScreen.getGameStage().addActor(gameManager.getBackgroundLayer01());
        gameScreen.getGameStage().addActor(gameManager.getSprinkle());
        gameScreen.getGameStage().addActor(gameManager.uiTable);
        gameScreen.getGameStage().addActor(gameManager.getThrowWeapon());
        gameScreen.getGameStage().addActor(gameManager.player);
    }
    //Функция ввода в игру "врагов"
    private void addEnemiesToGame(float delta){
        //вводим врагов в игру
        gameTime += delta;
        for (int i = 0; i < gameManager.time.size;i++){
            if ((int)gameTime == gameManager.time.get(i)){
                buildEnemies(i);
                gameManager.time.removeIndex(i);
                gameManager.typeEnemie.removeIndex(i);
                gameManager.posX.removeIndex(i);
                gameManager.posY.removeIndex(i);
                gameManager.weight.removeIndex(i);
                gameManager.speed.removeIndex(i);
            }
        }
    }
    //Функция создания классов "врагов"
    private void buildEnemies(int i){
            //String s = gameManager.name.get(i);

        //System.out.println("Height: "+gameManager.virusAtlas);
        if(gameManager.typeEnemie.get(i).trim().equals("v")){
            microbe = new Virus(new Vector2(gameManager.posX.get(i),gameManager.posY.get(i) * Gdx.graphics.getHeight()),gameManager.speed.get(i),
                    gameManager.weight.get(i),gameManager.player,gameManager.virusAtlas, gameManager.virusBulletAtlas, gameManager.getLifeScaleAtlas());
            gameManager.getEnemies().add(microbe);
            gameScreen.getGameStage().addActor(microbe);
        }
        if(gameManager.typeEnemie.get(i).trim().equals("b"))
        {
            colony = new BacteriasColony(new Vector2(gameManager.posX.get(i) * Gdx.graphics.getWidth(),
                    gameManager.posY.get(i) * Gdx.graphics.getHeight()), gameManager.getBacteriumAtlas());
            //if (colony != null)
            System.out.println("Colony create");
                gameManager.getBacteriasColonys().add(colony);

        }
        if(gameManager.typeEnemie.get(i).trim().equals("c")){
            microbe = new Condom(new Vector2(gameManager.posX.get(i), gameManager.posY.get(i) * Gdx.graphics.getHeight()),
                    gameManager.getCondomAtlasesArray(), gameManager.getLifeScaleAtlas());
            System.out.println("Condom create");
            gameManager.getEnemies().add(microbe);
            gameScreen.getGameStage().addActor(microbe);
        }
    }
    private void controlEnemiesPosition(){
        //отслеживание позиции врагов и удаление их со сцены если они вышли за пределы экрана
        for (int i = 0; i < gameManager.getEnemies().size; i++){
            if (gameManager.getEnemies().get(i) != null){
                if (gameManager.getEnemies().get(i).getPositionX() > gameScreen.getGameStage().getWidth()
                        || gameManager.getEnemies().get(i).getPositionX() < 0
                        || gameManager.getEnemies().get(i).getPositionY() > gameScreen.getGameStage().getHeight()
                        || gameManager.getEnemies().get(i).getPositionY() < 0) {
                    if (gameManager.getEnemies().get(i).remove()) {              //удаляем объект(Actor) с индексом i из сцены
                        removedActor = gameManager.getEnemies().removeIndex(i);  //удаляет объект с индексом i из массива и возвращает объект
                        removedActor = null; // присваиваем объекту null чтобы его уничтожил сборщик мусора (в Java не нужно самому удалять объекты из памяти)
                        //такой принцип применяем для всех игровых объектов
                    }
                }
            }
        }
    }
    private void addBulletsToArray() {
        //добавляем пули врагов в общий массив пуль для дальнейшей обработки
        //затем массивы пуль каждого врага очищаем
        //отдельный массив пуль нуже для того чтобы пули не были привязаны к врагам и не уничтожались при уничтожении врага,
        //а прожили свой жизненный цикл
        for (int i = 0; i < gameManager.getEnemies().size; i++) {
            if (gameManager.getEnemies().get(i) != null) {
                for (int j = 0; j < gameManager.getEnemies().get(i).getBulletsArray().size; j++) {
                    if (gameManager.getEnemies().get(i).getBulletsArray().get(j) != null)
                        gameManager.getBullets().add(gameManager.getEnemies().get(i).getBulletsArray().get(j));
                }
                gameManager.getEnemies().get(i).getBulletsArray().clear();
            }
        }
    }
    private void addBulletsToGame(){
        //выводим пули врагов на игровую сцену
            for (int j = 0; j < gameManager.getBullets().size; j++) {
                if (gameManager.getBullets().get(j) != null) {
                    gameScreen.getGameStage().addActor(gameManager.getBullets().get(j));
                }
            }
    }
    private void controlBulletPosition(){
        //отслеживание позиции пуль вирусов и удаление их со сцены если они вышли за пределы экрана
        for (int i = 0; i < gameManager.getBullets().size; i++){
            if (gameManager.getBullets().get(i) != null){
                if (gameManager.getBullets().get(i).getPositionX() > gameScreen.getGameStage().getWidth()
                        || gameManager.getBullets().get(i).getPositionX() < 0
                        || gameManager.getBullets().get(i).getPositionY() > gameScreen.getGameStage().getHeight()
                        || gameManager.getBullets().get(i).getPositionY() < 0) {
                    if (gameManager.getBullets().get(i).remove()) {
                        VirusBullet removedBullet = gameManager.getBullets().removeIndex(i);
                        removedBullet.clear();
                    }
                }
            }
        }
    }
    private void addWeaponToGame(){
        //вводим массив топоров для дальнейшей обработки
        for (int i = 0; i < gameManager.getThrowWeapon().getWeaponArray().size; i++){
            if (gameManager.getThrowWeapon().getWeaponArray().get(i) != null){
                gameManager.weapons.add(gameManager.getThrowWeapon().getWeaponArray().get(i));
            }
        }
        gameManager.getThrowWeapon().getWeaponArray().clear();
        //выводим массив топоров на игровую сцену
        for (int i = 0; i < gameManager.weapons.size; i++){
            if (gameManager.weapons.get(i) != null){
                gameScreen.getGameStage().addActor(gameManager.weapons.get(i));
            }
        }
    }
    private void controlWeaponPosition(){
        for (int i = 0; i < gameManager.weapons.size; i++){
            if (gameManager.weapons.get(i) != null){
                if(gameManager.weapons.get(i).getPositionX() < 50) {
                    if (gameManager.weapons.get(i).remove()) {
                        removedAxe = gameManager.weapons.removeIndex(i);
                        removedAxe = null;
                    }
                }
            }
        }
    }
    //ВВод сперматозоидов в игру
    private void addSpermsToGame(){
        //вводим массив сперматозоидов для дальнейшей обработки
        for (int i = 0; i < gameManager.getSprinkle().getSpermArray().size; i++){
            if (gameManager.getSprinkle().getSpermArray().get(i) != null){
                gameManager.sperms.add(gameManager.getSprinkle().getSpermArray().get(i));
            }
        }
        gameManager.getSprinkle().getSpermArray().clear();
        //выводим массив сперматозоидов на игровую сцену
        gameManager.setSpermAmount(gameManager.sperms.size);
        gameManager.updateSpermAmount();
        for (int i = 0; i < gameManager.sperms.size; i++){
            if (gameManager.sperms.get(i) != null){
                gameScreen.getGameStage().addActor(gameManager.sperms.get(i));
            }
        }
    }
    //Ввод колонии бактерий в игру
    private void addBacteriumToGame(){
        if (gameManager.getBacteriasColonys() != null) {
            for (int i = 0; i < gameManager.getBacteriasColonys().size; i++) {
                for (int j = 0; j < gameManager.getBacteriasColonys().get(i).getBacteriumArray().size; j++) {
                    if (gameManager.getBacteriasColonys().get(i).getBacteriumArray().get(j) != null) {
                            gameManager.getEnemies().add(gameManager.getBacteriasColonys().get(i).getBacteriumArray().get(j));
                            gameScreen.getGameStage().addActor(gameManager.getBacteriasColonys().get(i).getBacteriumArray().get(j));


                        //gameManager.getBacteriasColonys().get(i).setNext(true);
                        //System.out.println("set next true:");
                        //if (gameManager.getBacteriasColonys().get(i).getNext_item()) {

                           // System.out.println("Bacterium colony: "+ gameManager.getBacteriasColonys().get(i)+
                           //         " bact: " + gameManager.getBacteriasColonys().get(i).getBacteriumArray().get(j));
                          //  gameManager.getBacteriasColonys().get(i).setNext_item(false);
                          //  gameManager.getBacteriasColonys().get(i).setNext(false);
                        //}
                    }
                }
            }
            gameManager.getBacteriasColonys().clear();
        }
    }
    private void controlHitParticleEffect(){
        for (int i = 0; i < gameManager.hitParticleEffectArray.size; i++){
            if (gameManager.hitParticleEffectArray.get(i).isComplete()){
                gameManager.hitParticleEffectArray.get(i).resetEffect();
                gameManager.hitParticleEffectArray.get(i).remove();
                removedHPE = gameManager.hitParticleEffectArray.removeIndex(i);
                removedHPE = null;
            }
        }
    }
    private void addHitParticleEffectToGame(){
        for (int i = 0; i < gameManager.hitParticleEffectArray.size; i++) {
            gameScreen.getGameStage().addActor(gameManager.hitParticleEffectArray.get(i));
            gameManager.hitParticleEffectArray.get(i).allowCompletionEffect();
            gameManager.hitParticleEffectArray.get(i).startEffect();
        }
    }
    private void controlScoreCloud(){
        for (int i = 0; i < gameManager.scoreCloudArray.size; i++) {
            if(gameManager.scoreCloudArray.get(i).getY() > gameScreen.getGameStage().getHeight()){
                gameManager.scoreCloudArray.get(i).remove();
                removedScoreCloud  = gameManager.scoreCloudArray.removeIndex(i);
                removedScoreCloud = null;
            }
        }
    }
    private void addScoreCloudToGame(){
        for (int i = 0; i < gameManager.scoreCloudArray.size; i++) {
            gameScreen.getGameStage().addActor(gameManager.scoreCloudArray.get(i));
        }
    }
    private void controlLifeScale(){
        if (gameManager.player.getLifeCount() == 0)
            gameScreen.setGameState(4); //Game over
    }
    private void addBacteriophageToGame(){
        for (int i = 0; i < gameManager.getBacteriophages().size; i++) {
            if (gameManager.getBacteriophages().get(i) != null)
                gameScreen.getGameStage().addActor(gameManager.getBacteriophages().get(i));
        }
    }
    private void addOvumToGame(){
        if (gameManager.getOvum() != null){
            for (int i = 0; i < gameManager.sperms.size; i++){
                if (gameManager.sperms.get(i).getPositionX() <= gameScreen.getGameStage().getWidth() -
                        (gameScreen.getGameStage().getWidth()/3.0f)*2.0f)
                    gameScreen.getGameStage().addActor(gameManager.getOvum());

            }
        }
    }
    private void controlLevelEnd(){
        if(gameManager.getLevelEnd()){
            gameScreen.setStringLevelParamsSave(true);
            gameScreen.setGameState(3);
        } //Level end
    }
    //Изменение переменной delta_time_particle_effect которая применяется для
    //задержки окончание уровня до конца отрисовки еффекта ovum_effect
    private void changeDeltaTimeParticleEffect(float delta){
        gameManager.setDeltaTimeParticleEffect(gameManager.getDeltaTimeParticleEffect() - delta);
    }

}
