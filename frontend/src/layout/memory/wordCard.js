import React from 'react';
import { Progress, Row } from 'antd';
import CardList from './card';
import {list, forget, remeber, finish} from '../../action/cardAction';
import { Tag, Divider } from 'antd';


const renderFront = (word) => {

    const renderSentence = () => {
        if (word.sentences == null || word.sentences.length == 0) return null;
        return   (
        <Row style={{marginBottom: '1rem'}}>
                <Tag color="green">例句</Tag>
                {word.sentences[parseInt(Math.random() * word.sentences.length)].sentence}
        </Row>) 
    }
    
    return (
        <div>
            {renderSentence()}
            <Row>
                <Tag color="blue">单词</Tag>{word.word}
            </Row>            
        </div>
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
            <Progress type="circle" percent={
                countRemember == 0 ? 
                0 :
                (countRemember / count * 100).toFixed(1)
            } 
                />
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