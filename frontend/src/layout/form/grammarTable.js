import React, {useState, useEffect} from 'react';
import { Form, Input, Button, Table, Modal} from 'antd';
import { MinusCircleOutlined, PlusOutlined } from '@ant-design/icons';
import TextArea from 'antd/lib/input/TextArea';
import {add, list, remove, modify} from '../../action/grammarAction';
import {ModalForm, useModalForm} from './modalForm';


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

export const GrammarForm = ({form, onSubmit}) => {
  const onFinish = values => {
    onSubmit(values);
  };

  return (
    <Form form={form} name="dynamic_form_item" {...formItemLayoutWithOutLabel} onFinish={onFinish}>
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
              {console.log(field)}
                  <Form.Item
                    {...field}
                    name={[field.name, 'sentence']}
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


export function GrammarTable () {

const modalForm = useModalForm();
const [dataSource, setDataSource] = useState([]);

useEffect(() => {
  onList()  
}, [])

  const columns = [
    {
      title: '语法',
      dataIndex: 'grammar',
      key: 'grammar',
      render: text => <span>{text}</span>,
    }, 
    {
      title: '操作',
      key: 'opertaion',
      render: (text, record) => (
          <span>
              <Button type='primary' style={{marginRight: '5px'}} onClick={()=>modalForm.setModify(record)}>修改</Button>
              <Button type='danger' onClick={() => onDelete(record.id)}>删除</Button>
          </span>
      )
    }
  ]

  const onList = async () => {
    const dataSource = await list();
    setDataSource(dataSource);
  }

  const onAdd = async (values) => {
    await add(values);
    onList();
    modalForm.onCancel();
}

const onModify = async(values, preValues) => {
  console.log(values);
    values.id = preValues.id;
    await modify(values);
    onList();
    modalForm.onCancel();
}

const onDelete = async (id) => {
    await remove(id)
    onList();
}

  return (
    <div>
      <div className="table-operations">
        <Button onClick={modalForm.setAdd}>新增</Button>
        <ModalForm 
          {...modalForm}
          onAdd={onAdd} 
          onModify={onModify}>
          {(form) => <GrammarForm {...form} />}
        </ModalForm>
      </div>
      <Table columns={columns} dataSource={dataSource} rowKey={record => record.id}/>
    </div>
    )
}