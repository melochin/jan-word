import React from 'react';
import {Row, Col} from 'antd';
import { Button } from 'antd';
import { CaretRightOutlined } from '@ant-design/icons';

  
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

/**
 * 功能：卡片拥有正反两面，正面显示提问，反面显示答案。
 *               卡片默认显示正面，点击卡片时反转反面
 * 
 * props：
 *value，
 * renderFront
 */
const style = {
    textAlign: 'center', 
    height: '100%'
}

class Card extends React.Component {

    constructor(props) {
        super(props);
        this.handleClick = this.handleClick.bind(this)
        this.state = {
            isFront : true, 
        }
    }
    
    componentWillReceiveProps(props, state) {
        this.setState({
            isFront: true,
        })
    }

    handleClick() {
        if (this.state.isFront) {
            this.setState({
                isFront: false,
            })
        }
    }

    render() {
        return (
            <Row onClick={this.handleClick} 
                    justify="space-around" align="middle"  style={{...style}}>
                <Col span={24}>
                    {this.state.isFront && this.props.renderFront(this.props.value)}
                    {!this.state.isFront && this.props.renderBack(this.props.value)}
                </Col>
            </Row>
        )
    }

}

class EndCard extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <Row  justify="space-around" align="middle"   style={{height: '100%', textAlign: 'center'}}>
                <Col span={24}>
                    <p>{'おつかれ'}</p>
                    <p>{'（●´∀｀）♪'}</p>
                </Col>
            </Row>
        )
    }
}

class CardList extends React.Component  {

        constructor(props) {
            super(props)
            this.handleClick = this.handleClick.bind(this);
            this.state = {current: -1}
            this.list = [];
        }

        async componentDidMount() {
            // TODO 后台接口需要返回合适的数据格式，前端不调整数据格式
            let datasource = await this.props.list()
            this.list = datasource;
            this.setState({
                current: 0
            })
        }

        handleClick() {
                this.setState({
                    current: this.state.current + 1
                })
        }

        render() {
            return (
                <Row style={{height: '100%'}}   justify="space-around" align="middle"  >
                    <Col span={2}></Col>
                    <Col span={20} style={{height:'100%'}}>
                        {
                            this.state.current >= 0 && this.state.current < this.list.length &&
                            <Card value={this.list[this.state.current]} 
                                renderFront={this.props.renderFront} renderBack={this.props.renderBack}/>
                        }
                        {
                            this.state.current == this.list.length && 
                            <EndCard />
                        }
                    </Col>
                    <Col span={2}>
                        {
                            this.state.current < this.list.length &&
                            <Button size="large" onClick={this.handleClick} icon={<CaretRightOutlined />} ></Button>
                        }
                    </Col>
                </Row>
            )
        }
}


export default CardList;