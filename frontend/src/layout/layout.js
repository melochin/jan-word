import React from 'react';
import { Layout, Menu } from 'antd';
import { UserOutlined } from '@ant-design/icons';
import WordCard from './memory/wordCard.js';
import GrammarCard from './memory/grammarCard';
import {WordTable} from './form/wordTable.js';
import {GrammarTable} from './form/grammarTable';

import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link,
  withRouter
} from "react-router-dom";

const { SubMenu } = Menu;
const { Header, Content, Footer, Sider } = Layout;

class SiderMenu extends React.Component{
  
  constructor(props) {
    super(props);
    console.log(props.location.pathname.split("/"));
  }

  render() {

    let path = this.props.location.pathname.split("/");

    return (
      <Sider       
        breakpoint="md" collapsedWidth="0" 
        className="site-layout-background" width={'10rem'}>
          <Menu
          mode="inline"
          defaultSelectedKeys={ this.props.location.pathname}
          defaultOpenKeys={[path[1]]}
          style={{ height: '100%' }}
        >
          <SubMenu
            key="card"
            title={
              <span>
                <UserOutlined />
                记忆
              </span>
            }
          >
            <Menu.Item key="/card/word">
              <Link to="/card/word">单词</Link>
              </Menu.Item>
            <Menu.Item key="/card/grammar">
                <Link to="/card/grammar">
                  语法
                </Link>
              </Menu.Item>
          </SubMenu>
          <SubMenu
            key="data"
            title={
              <span>
                维护
              </span>
            }
          >
            <Menu.Item key="/data/word">
              <Link to="/data/word">单词</Link>
            </Menu.Item>
            <Menu.Item key="/data/grammar">
              <Link to="/data/grammar">语法</Link>
            </Menu.Item>
          </SubMenu>
        </Menu> 
      </Sider>
  )
  }
}

function Main(props) {

    return (
        <Router>
        <Layout>
        <Header className="header">
          <div className="logo" />
          <Menu
            theme="dark"
            mode="horizontal"
            defaultSelectedKeys={['2']}
            style={{ lineHeight: '64px' }}
          >
            <Menu.Item key="1">nav 1</Menu.Item>
            <Menu.Item key="2">nav 2</Menu.Item>
            <Menu.Item key="3">nav 3</Menu.Item>
          </Menu>
        </Header>
        <Content style={{ padding: '0 50px' }}>
          {/* <Breadcrumb style={{ margin: '16px 0' }}>
            <Breadcrumb.Item>Home</Breadcrumb.Item>
            <Breadcrumb.Item>List</Breadcrumb.Item>
            <Breadcrumb.Item>App</Breadcrumb.Item>
          </Breadcrumb> */}
          <Layout className="site-layout-background" style={{ padding: '1rem 0' }}>
            {withRouter(SiderMenu)(props)}
            <Content style={{ margin:'0 10px', padding: '0 24px', minHeight: 280, backgroundColor: 'white' }}>
              <Switch>
                <Route exact path="/card/word">
                  <WordCard/>
                </Route>
                <Route exact path="/card/grammar">
                  <GrammarCard />
                </Route>
                <Route exact path="/data/word">
                    <WordTable/>
                </Route>
                <Route exact path="/data/grammar">
                    <GrammarTable />
                </Route>
              </Switch>
            </Content>
          </Layout>
        </Content>
        <Footer style={{ textAlign: 'center' }}>Word ©2018 Created by Kazechin</Footer>
      </Layout>
      </Router>
    )
}






export default Main;