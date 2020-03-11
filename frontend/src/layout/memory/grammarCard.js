import React from 'react';
import CardList from './card';
import {list} from '../../action/grammarAction';

const renderFront = (grammar) => {
    let index = parseInt(Math.random() * grammar.sentences.length);

    return (
        <p>{grammar.sentences[index].sentence}</p>
    )
}

const renderBack = (grammar) => {
    return (
        <div>
            <p>{grammar.grammar}</p>
            <p>{grammar.detail}</p>
        </div>
    )
}

const GrammarWord = () => {
    return (
        <CardList list={list} 
            renderFront={renderFront} renderBack={renderBack}/>
    )
}

export default GrammarWord;