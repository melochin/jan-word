import React, {useState, useEffect} from 'react';
import { Layout, Menu, Row, Col, Dropdown, Button } from 'antd';
import { UserOutlined, SmileTwoTone} from '@ant-design/icons';
import WordCard from './memory/wordCard.js';
import GrammarCard from './memory/grammarCard';
import {WordTable} from './form/wordTable.js';
import {GrammarTable} from './form/grammarTable';
import Login from './login';
import MemoryCalendar from './user/MemoryCalendar';
import ProtectedRouter from './ProtectedRouter';
import {removeUserInStorage, getUserFromStorage} from '../action/userAction';
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link,
  withRouter,
  useHistory
} from "react-router-dom";

const { SubMenu } = Menu;
const { Header, Content, Footer, Sider } = Layout;

const SiderMenu = ({location}) => {
  let path = location.pathname.split("/");
    return (
      <Sider       
        breakpoint="md" collapsedWidth="0" 
        className="site-layout-background" width={'10rem'}>
          <Menu
          mode="inline"
          defaultSelectedKeys={ location.pathname}
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



const MyHeader = () => {
  const history = useHistory();
  const user = getUserFromStorage();
  const renderMenu = () => ( 
    <Menu >
      <Menu.Item key="user">
  <Link to="/user">{user.username}</Link>
      </Menu.Item>
      <Menu.Item key="signout">
        <Button onClick={() => {
            removeUserInStorage();
            history.replace("/login");
        }}>登出</Button>
      </Menu.Item>
    </Menu>
  )

  return (
    <Row justify="end">
      <Col span={4}>
      <Dropdown overlay={renderMenu()} >
        <Button style={{border: "none"}}>
          <SmileTwoTone style={{fontSize: "1.2rem"}}/>
        </Button>
      </Dropdown>
      </Col>
    </Row>
  )
}

function Main(props) {
    return (
        <Router>
            <Route exact path="/login">
                <Login ></Login>
              </Route>
              <ProtectedRouter exact path={['/', '/card/*', '/data/*', '/user']}>
                  <Layout>
            <Header className="header" style={{backgroundColor: 'white', borderBottom: '1rem', borderBottomColor:'grey' }}>
              <MyHeader />
            </Header>
            <Content style={{ padding: '0 50px' }}>
              {/* <Breadcrumb style={{ margin: '16px 0' }}>
                <Breadcrumb.Item>Home</Breadcrumb.Item>
                <Breadcrumb.Item>List</Breadcrumb.Item>
                <Breadcrumb.Item>App</Breadcrumb.Item>
              </Breadcrumb> */}
              <Layout className="site-layout-background" style={{ padding: '1rem 0' }}>
                {withRouter(SiderMenu)(props)}
                <Content id="content" style={{ margin:'0 10px', padding: '0 24px', backgroundColor: 'white' }} >
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
                    <Route exact path="/user">
                        <MemoryCalendar/>
                    </Route>
                  </Switch>
                </Content>
              </Layout>
            </Content>
            <Footer style={{ textAlign: 'center' }}>Jan Word ©2020 Created by Kazechin</Footer>
          </Layout>
              </ProtectedRouter>
      </Router>
    )
}


export default Main;