import React from 'react';
import CardList from './card';
import {list, forget, remeber, finish} from '../../action/cardAction';

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
        <CardList list={list}  remeber={remeber} forget={forget} finish = {finish} 
            renderFront={renderFront} renderBack={renderBack}/>
    )
}

export default WordCard;