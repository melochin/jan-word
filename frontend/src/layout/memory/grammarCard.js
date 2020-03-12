import React from 'react';
import CardList from './card';
import {list, forget, remeber, finish} from '../../action/gammraCardAction';

const renderFront = (grammar) => {
    if (grammar.sentences == null) return null;
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
        <CardList list={list}  remeber={remeber} forget={forget} finish = {finish} 
            renderFront={renderFront} renderBack={renderBack}/>
    )
}

export default GrammarWord;