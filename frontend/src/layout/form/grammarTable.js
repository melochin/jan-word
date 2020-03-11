import React from 'react';
import { Form, Input, Button, Table, Modal} from 'antd';
import { MinusCircleOutlined, PlusOutlined } from '@ant-design/icons';
import TextArea from 'antd/lib/input/TextArea';
import {add, list, remove} from '../../action/grammarAction';
import {ModalForm} from './modalForm';


const formItemLayout = {
  labelCol: {
    xs: { span: 24 },
    sm: { span: 4 },
  },
  wrapperCol: {
    xs: { span: 24 },
    sm: { span: 10 },
  },
};
const formItemLayoutWithOutLabel = {
  wrapperCol: {
    xs: { span: 24, offset: 0 },
    sm: { span: 10, offset: 4 },
  },
};

export const GrammarForm = ({onSubmit}) => {
  const onFinish = values => {
    if (values.hasOwnProperty('sentences')) {
      values.sentences = values.sentences.map( s => ({sentence: s}))
    }
    onSubmit(values);
  };

  return (
    <Form name="dynamic_form_item" {...formItemLayoutWithOutLabel} onFinish={onFinish}>
        <Form.Item name="grammar" label="语法" {...formItemLayout}>
            <Input />
        </Form.Item>
        <Form.Item name="detail" label="说明" {...formItemLayout}>
            <TextArea />
        </Form.Item>
      <Form.List name="sentences">
        {(fields, { add, remove }) => {
          return (
            <div>
              {fields.map((field, index) => (
                <Form.Item
                  {...(index === 0 ? formItemLayout : formItemLayoutWithOutLabel)}
                  label={index === 0 ? '例句' : ''}
                  required={false}
                  key={field.key}
                >
                  <Form.Item
                    {...field}
                    validateTrigger={['onChange', 'onBlur']}

                    rules={[
                      {
                        required: true,
                        whitespace: true,
                        message: "请输入例句",
                      },
                    ]}
                    noStyle
                  >
                    <Input style={{ width: '80%', marginRight: 8 }} />
                  </Form.Item>
                  {fields.length > 0 ? (
                    <MinusCircleOutlined
                      className="dynamic-delete-button"
                      onClick={() => {
                        remove(field.name);
                      }}
                    />
                  ) : null}
                </Form.Item>
              ))}
              <Form.Item>
                <Button
                  type="dashed"
                  onClick={() => {
                    add();
                  }}
                  style={{ width: '60%' }}
                >
                  <PlusOutlined /> 新增例句
                </Button>
              </Form.Item>
            </div>
          );
        }}
      </Form.List>

      <Form.Item>
        <Button type="primary" htmlType="submit">
          Submit
        </Button>
      </Form.Item>
    </Form>
  );
};


export class GrammarTable extends React.Component {

  constructor(props) {
    super(props);
    this.state = {dataSource: [], visible: false};
    this.onAdd = this.onAdd.bind(this);
    this.columns = [{
        title: '语法',
        dataIndex: 'grammar',
        key: 'grammar',
        render: text => <span>{text}</span>,
    }, {
      title: '操作',
      key: 'opertaion',
      render: (text, record) => (
          <span>
              <Button disabled="disabled" type='primary' style={{marginRight: '5px'}}>修改</Button>
              <Button type='danger' onClick={() => this.onDelete(record.id)}>删除</Button>
          </span>
      )
    }]
  }

  componentDidMount() {
    this.onList();
  }

  async onList() {
    let dataSource = await list();
    this.setState({dataSource});
  }

  async onDelete(id) {
    await remove(id);
    this.onList();
  }

  async onAdd(value) {
    await add(value);
    this.onList();
    this.setState({
      visible: false
    })
  }

  render() {
    const {visible} = this.state;
    return (
      <div>
        <Modal visible={visible} onCancel={() => this.setState({visible: false})}>
          <GrammarForm onSubmit={this.onAdd}/>
        </Modal>
        <Button onClick={() => this.setState({visible: true})}>新增</Button>
        <Table columns={this.columns} dataSource={this.state.dataSource} />
      </div>
    )
  }
}