import React from 'react';
import {Row, Col, Button} from 'antd';
import { CaretLeftOutlined, CaretRightOutlined } from '@ant-design/icons';

const Card = ({value}) => {
    return (
        <Row justify="space-around" align="middle"  style={{height: '100%', textAlign: 'center'}}>
            <Col span={24} style={{fontSize: "1.5rem"}}>
                {value}
            </Col>
        </Row>
    )
}

const EndCard = () => {
    const ele = (
        <div>
                <p>{'おつかれ'}</p>
                <p>{'（●´∀｀）♪'}</p>
        </div>
    )
    return (<Card value={ele} />);
}

/**
 * 
 */
class CardList extends React.Component  {

        constructor(props) {
            super(props)
            this.onNextCard = this.onNextCard.bind(this);
            this.onKeyDown = this.onKeyDown.bind(this);
            this.onTurnCard = this.onTurnCard.bind(this);
            this.onForget = this.onForget.bind(this);
            this.onRemember = this.onRemember.bind(this);
            this.state = {current:  new Object(), front: true, start: false, count: 0, countRemember: 0}
            this.list = [];
            this.finish = false;
        }

        async componentDidMount() {
            let data = await this.props.list()
            this.list = data.datasource;
            this.setState({
                current: this.list != null ? this.list.pop() : null,
                count: data.count,
                countRemember: data.countRemember
            })
            document.addEventListener('keydown', this.onKeyDown);
        }

        async onNextCard() {
                let current = this.list.pop();
                if (current == null && this.finish == false)   {
                    this.finish = true;
                    await this.props.finish();
                }
                this.setState({
                    front: true,
                    current: current
                })
        }

        /**
         * 
         * @param {*} e 
         */
        onKeyDown(e) {
            if (this.state.start == false) return;
            switch(e.keyCode) {
                case 37:
                    if (this.state.front == false) {
                        // 左边箭头，忘记
                        this.onForget();
                    }
                    break;
                case 39:
                    if (this.state.front) {
                        // 右边箭头，翻卡
                        this.onTurnCard();
                    } else {
                        // 右边箭头，记得
                        this.onRemember();
                    }
                    break;
            }
        }

        async onRemember() {
            let isRemembered = await this.props.remeber(this.state.current.id);
            if (isRemembered == false) {
                this.list.unshift(this.state.current);
            }
            this.onNextCard();
        }

        async onForget() {
            await this.props.forget(this.state.current.id);
            this.list.unshift(this.state.current);
            this.onNextCard();
        }

        onTurnCard() {
            if (this.state.start == false) return;
            // 正面且不是最后一个，才可以翻转卡片
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

        renderStart() {
            return (
                <React.Fragment>
                {this.props.renderStart(this.state.count, this.state.countRemember)}
                {
                    this.state.countRemember  < this.state.count &&
                    <Button type="primary" onClick={() => this.setState({start:true})}>开始</Button>
                }
                </React.Fragment>
            )
        }

        render() {
            return (
                <Row style={{height: '100%'}}   justify="space-around" align="middle" onClick={this.onTurnCard} >
                    <Col span={2}></Col>
                    <Col span={20} align="middle" style={{height: '100%'}}>
                        <Col span={24} style={{height: '25%'}}></Col>

                        <Col span={24} style={{height:'50%'}} >
                            {this.state.start == false ? this.renderStart() : this.renderCard()}
                        </Col>
                        <Col span={24} style={{height:'25%'}}  >
                            {this.state.front == false &&
                            <React.Fragment>
                                <Button type="danger" onClick={this.onForget} style={{marginRight: '1rem'}}>
                                    <CaretLeftOutlined /> 忘记
                                </Button>
                                <Button type="primary" onClick={this.onRemember}>
                                    记得 <CaretRightOutlined />
                                </Button>
                            </React.Fragment>
                            }
                        </Col>
                    </Col>
                    <Col span={2}></Col>
                </Row>
            )
        }
}


export default CardList;