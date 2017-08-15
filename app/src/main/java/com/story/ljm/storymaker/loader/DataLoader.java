package com.story.ljm.storymaker.loader;

import com.story.ljm.storymaker.abstraction.IDataLoader;
import com.story.ljm.storymaker.dao.Character;
import com.story.ljm.storymaker.dao.Concept;
import com.story.ljm.storymaker.dao.Place;
import com.story.ljm.storymaker.dao.Story;
import com.story.ljm.storymaker.manager.DatabaseManager;
import com.story.ljm.storymaker.util.Constant;

/**
 * Created by ljm on 2017-05-15.
 */

public class DataLoader implements IDataLoader{
    private static DataLoader loader;
    private Story mStory;
    private Character mCharacter;
    private Place mPlace;
    private Concept mConcept;
    private DatabaseManager mDBManager;

    private DataLoader(){
       mDBManager = DatabaseManager.getInstance();
    }

    public static DataLoader getInstance(){

        if(loader == null){
            loader = new DataLoader();
        }

        return loader;
    }

    @Override
    public void loadData() {
        Story story = new Story();
        story.parseData(mDBManager.getQueryResult());
        setStory(story);

    }

    public void loadResourceData(int storyID){
        //스토리를 만들 때 리소스를 저장할 테이블을 생성
        DatabaseManager.getInstance().createCharacterResourceTable(storyID);
        DatabaseManager.getInstance().createPlaceResourceTable(storyID);
        DatabaseManager.getInstance().createConceptResourceTable(storyID);

        Character character = new Character();
        character.parseData(mDBManager.getResourceQuery(Constant.NAME_TABLE_CHARACTER, storyID));
        setCharacter(character);

        Place place = new Place();
        place.parseData(mDBManager.getResourceQuery(Constant.NAME_TABLE_PLACE, storyID));
        setPlace(place);

        Concept concept = new Concept();
        concept.parseData(mDBManager.getResourceQuery(Constant.NAME_TABLE_CONCEPT, storyID));
        setConcept(concept);
    }

    public void loadCharacterData(int storyID){
        Character character = new Character();
        character.parseData(mDBManager.getResourceQuery(Constant.NAME_TABLE_CHARACTER, storyID));
        setCharacter(character);
    }

    public void loadPlaceData(int storyID){
        Place place = new Place();
        place.parseData(mDBManager.getResourceQuery(Constant.NAME_TABLE_PLACE, storyID));
        setPlace(place);
    }

    public void loadConceptData(int storyID){
        Concept concept = new Concept();
        concept.parseData(mDBManager.getResourceQuery(Constant.NAME_TABLE_CONCEPT, storyID));
        setConcept(concept);
    }

    public Story getStory() {
        return mStory;
    }

    public void setStory(Story mStory) {
        this.mStory = mStory;
    }

    public Character getCharacter() {
        return mCharacter;
    }

    public void setCharacter(Character mCharacter) {
        this.mCharacter = mCharacter;
    }

    public Place getPlace() {
        return mPlace;
    }

    public void setPlace(Place mPlace) {
        this.mPlace = mPlace;
    }

    public Concept getConcept() {
        return mConcept;
    }

    public void setConcept(Concept mConcept) {
        this.mConcept = mConcept;
    }
}
