import React from 'react';
import { Progress } from 'antd';
import CardList from './card';
import {list, forget, remeber, finish} from '../../action/cardAction';

const renderFront = (word) => {
    return (
        <p>{word.word}</p>
    )
}

const renderBack =  (word) => {
    return (
        <div>
            假名：{word.gana}<br/>
            中文：{word.chinese}
        </div>
    )
}

const renderStart = (count, countRemember) => {

    return (
        <div style={{marginBottom: '1rem'}}>
            <Progress type="circle" percent={(countRemember / count * 100).toFixed(1)} />
        </div>
    )
}

const WordCard = () => {
    return (
        <CardList list={list}  remeber={remeber} forget={forget} finish = {finish} 
            renderStart={renderStart} renderFront={renderFront} renderBack={renderBack}/>
    )
}

export default WordCard;