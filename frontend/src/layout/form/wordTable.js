import React, {useState, useEffect}from 'react';
import { Form, Input, Button } from 'antd';
import { Table } from 'antd';
import {ModalForm, useModalForm} from './modalForm';
import {add, remove, list, modify} from '../../action/wordAction.js';

const layout = {
    labelCol: { span: 6 },
    wrapperCol: { span: 12 },
  };

const WordForm = ({form, onSubmit}) => {
    return (
        <Form form={form} onFinish={onSubmit} {...layout}>
            {items}
        </Form>
    )
}

const items = [
    (
        <Form.Item name="word" label="日语"
            rules={[
                {
                    required: true,
                    message: '请输入日语'
                },
            ]}
        >
        <Input />
    </Form.Item>
    ), (
        <Form.Item name="gana" label="假名">
            <Input />
        </Form.Item>
    ),
    (
        <Form.Item name="chinese" label="中文"
            rules={[
                {
                    required: true,
                    message: '请输入中文'
                },
            ]}
        >
            <Input />
        </Form.Item>
    ),
    (
        <Form.Item wrapperCol={{ ...layout.wrapperCol, offset: 6 }} >
            <Button type="primary" htmlType="submit">提交</Button>
        </Form.Item>
    )
]


export function WordTable() {

    const modalForm = useModalForm();
    const [dataSource, setDataSource] = useState([]);

    useEffect(() => {
      onList()  
    }, [])

    const columns = [
        {
          title: '日语',
          dataIndex: 'word',
          key: 'word',
          render: text => <span>{text}</span>,
        },
        {
            title: '假名',
            dataIndex: 'gana',
            key: 'gana',
            render: text => <span>{text}</span>,
          },
          {
            title: '中文',
            dataIndex: 'chinese',
            key: 'chinese',
            render: text => <span>{text}</span>,
          },
          {
              title: '操作',
              key: 'operation',
              render: (text, record) => (
                  <span>
                      <Button  type='primary' style={{marginRight: '5px'}}
                        onClick={() => modalForm.setModify(record)}
                      >修改</Button>
                      <Button type='danger' onClick={() => onDelete(record.id)}>删除</Button>
                  </span>
              )
          }
    ];

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
                        {(form) => <WordForm {...form} />}
                </ModalForm>
            </div>
        <Table columns={columns} dataSource={dataSource} rowKey={record => record.id}/>
        </div>
    )
}