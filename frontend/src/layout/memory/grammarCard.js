import React from 'react';
import CardList from './card';
import {list, forget, remeber, finish} from '../../action/gammraCardAction';
const ReactMarkdown = require('react-markdown')

const renderFront = (grammar) => {
    if (grammar.sentences == null) return null;
    let index = parseInt(Math.random() * grammar.sentences.length);
    return (
        <ReactMarkdown source={grammar.sentences[index].sentence} />
    )
}

const renderBack = (grammar) => {
    return (
        <div>
            {grammar.grammar}<br/>
            {grammar.detail}
        </div>
    )
}

const renderStart = () => {
    return (
        <div>
        </div>
    )
}

const GrammarWord = () => {
    return (
        <CardList list={list}  remeber={remeber} forget={forget} finish = {finish} 
        renderStart={renderStart} renderFront={renderFront} renderBack={renderBack}/>
    )
}

export default GrammarWord;