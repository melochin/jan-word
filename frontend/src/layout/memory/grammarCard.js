import React from 'react';
import CardList from './card';
import {list, forget, remeber, finish} from '../../action/gammraCardAction';
import ReactDOMServer from 'react-dom/server';
const ReactMarkdown = require('react-markdown')

const renderFront = (grammar) => {
    if (grammar.sentences == null) return null;
    let index = parseInt(Math.random() * grammar.sentences.length);
    // 因为存在Markdown特殊字符的缘故，将它们转换成HTML字符串
    // 然后禁止ReactMarkdown转义HTML即可渲染。
    const eles = grammar.sentences[index].readings.map(reading => 
        reading[1] == '' ? 
        reading[0] :
        `<ruby>${reading[0]}<rt>${reading[1]}</rt></ruby>`
    ).join('');
    console.log(eles);
    return (
        <React.Fragment>
            <ReactMarkdown source={eles} escapeHtml={false}/>
        </React.Fragment>
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