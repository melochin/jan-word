import React from 'react';
import CardList from './card';
import {list} from '../action/wordAction';

const renderFront = (word) => {
    return (
        <p>{word.word}</p>
    )
}

const renderBack = (word) => {
    return (
        <div>
            <p>{word.gana}</p>
            <p>{word.chinese}</p>
        </div>
    )

}

const WordCard = () => {
    return (
        <CardList list={list} renderFront={renderFront} renderBack={renderBack}/>
    )
}

export default WordCard;