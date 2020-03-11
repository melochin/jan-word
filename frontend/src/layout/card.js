import React from 'react';
import {Row, Col} from 'antd';
import { Button } from 'antd';
import { CaretLeftOutlined, CaretRightOutlined } from '@ant-design/icons';

/**
 * 功能：卡片拥有正反两面，正面显示提问，反面显示答案。
 *               卡片默认显示正面，点击卡片时反转反面
 * 
 * props：
 *value，
 * renderFront
 */
class Card extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <Row onClick={this.handleClick} 
                    justify="space-around" align="middle"  style={{height: '100%', textAlign: 'center'}}>
                <Col span={24} style={{fontSize: "2rem"}}>
                    {this.props.value}
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
                <Col span={24} style={{fontSize: "2rem"}}>
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
            this.onKeyDown = this.onKeyDown.bind(this);
            this.onTurn = this.onTurn.bind(this);
            this.state = {current:  null, front: true}
            this.list = [];
        }

        async componentDidMount() {
            // TODO 后台接口需要返回合适的数据格式，前端不调整数据格式
            let datasource = await this.props.list()
            this.list = datasource;
            console.log(this.list);
            this.setState({
                current: this.list.pop()
            })
            document.addEventListener('keydown', this.onKeyDown);
        }

        handleClick() {
                this.setState({
                    front: true,
                    current: this.list.pop()
                })
        }

        onKeyDown(e) {
            switch(e.keyCode) {
                case 37:
                    // 卡片反面，左边箭头代表不知道
                    if (this.state.front == false) {
                        this.onWrong();
                    }
                    break;
                case 39:
                    // 卡片正面，右边箭头代表翻反面        
                    // 卡片反面，右边箭头代表知道
                    if (this.state.front) {
                        this.onTurn();
                    } else {
                        this.onRight();
                    }
                    break;
            }
        }

        onRight(val) {
            this.handleClick();
        }

        onWrong(val) {
            this.list.unshift(val);
            this.handleClick();
        }

        onTurn() {
            if (this.state.front && this.state.current != null) {
                this.setState({
                    front: false
                })
            }
        }

        renderCard() {
            if (this.state.current !=null) {
                return (
                    <Card value={
                        this.state.front ? 
                        this.props.renderFront(this.state.current) :
                        this.props.renderBack(this.state.current)
                    } />
                )
            } else {
                return (<EndCard />);
            }
        }

        render() {
            return (
                <Row style={{height: '100%'}}   justify="space-around" align="middle" onClick={this.onTurn} >
                    <Col span={2}></Col>
                    <Col span={20} align="middle" style={{height: '100%'}}>
                        <Col span={24} style={{height: '25%'}}></Col>
                        <Col span={24} style={{height:'50%'}} >
                            {this.renderCard()}
                        </Col>
                        <Col span={24} style={{height:'25%'}}>
                            {this.state.front == false &&
                            <div>
                                <Button type="danger" onClick={() => this.onWrong(this.state.current)} style={{marginRight: '1rem'}}>
                                    <CaretLeftOutlined /> 记忆错误
                                </Button>
                                <Button type="primary" onClick={() => this.onRight(this.state.current)}>
                                    记忆正确<CaretRightOutlined />
                                </Button>
                            </div>
                            }
                        </Col>
                    </Col>
                    <Col span={2}></Col>
                </Row>
            )
        }
}


export default CardList;