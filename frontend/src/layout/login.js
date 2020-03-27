import React from 'react';
import { Form, Input, Button, message, Row,Col} from 'antd';
import {login, setUserInStorage} from '../action/userAction';
import {useHistory, useLocation} from "react-router-dom";

const layout = {
  labelCol: {
    span: 6,
  },
  wrapperCol: {
    span: 18,
  },
};
const tailLayout = {
  wrapperCol: {
    offset: 6,
    span: 18,
  },
};

const Login = () => {

  let history = useHistory();
  let location = useLocation();

  // 跳转到之前的位置，默认是“/”
  let { from } = location.state || { from: { pathname: "/" } };
  console.debug(`from url:${ JSON.stringify(from)}`);
  const onFinish = values => {
        login(values)
            .then(user => {
                if (user == null   || user == undefined) return;
                // TODO 用户角色
                setUserInStorage(user);
                console.log(history.location);
                history.push(from);
            })
            .catch(err => {
                message.error(err.message)
            })
  };


  const onFinishFailed = errorInfo => {
  };

  return (
        <Row justify="space-around" align="middle" style={{height: '80%'}}>
           <Col md={7} xs={20} >
             <Row style={{textAlign: 'center', fontSize: '2rem', paddingBottom: '1rem'}} >
              <Col span={24}>Jan Word</Col>
             </Row>
              <Form
                style={{backgroundColor: '#eee', padding: '24px 2rem 24px 2rem', borderRadius: '0.5rem'}}
                {...layout}
                name="basic"
                initialValues={{
                remember: true,
                }}
                onFinish={onFinish}
                onFinishFailed={onFinishFailed}
                >
              <Form.Item
              label="用户名"
              name="username"
              rules={[
              {
              required: true,
              message: '请输入用户名！',
              },
              ]}
              >
              <Input />
              </Form.Item>

              <Form.Item
              label="密码"
              name="password"
              rules={[
              {
              required: true,
              message: '请输入密码！',
              },
              ]}
              >
              <Input.Password />
              </Form.Item>

              <Form.Item {...tailLayout}>
              <Button type="primary" htmlType="submit">
              登陆
              </Button>
              </Form.Item>
              </Form>

    </Col>

    </Row>

  );
};

export default Login;