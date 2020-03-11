import React, {useState, useEffect}from 'react';
import { Form, Input, Button } from 'antd';
import { Table } from 'antd';
import {ModalForm} from './modalForm';

import {add, remove, list, modify} from '../../action/wordAction.js';

const layout = {
    labelCol: { span: 6 },
    wrapperCol: { span: 12 },
  };

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

/**
 * 单词的维护表格（CRUD）
 *
 */
export function WordTable(props) {

    const [dataSource, setDataSource] = useState([]);
    const [visible, setVisible] = useState(false);
    const [initialValues, setInitialValues] = useState(new Object());
    const [mode, setMode] = useState('new');

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
                        onClick={() => openModify(record)}
                      >修改</Button>
                      <Button type='danger' onClick={() => onDelete(record.id)}>删除</Button>
                  </span>
              )
          }
    ];

    const openModify = (record) => {
        setVisible(true);
        setInitialValues(record);
        setMode('modify');
    }

    const onCreate = async (values) => {
        await add(values);
        onList();
        onCancel();
    }

    const onModify = async(values) => {
        await modify(values);
        onList();
        onCancel();
    }

    const onCancel = () => {
        setInitialValues(new Object());
        setVisible(false);
      }

    const onDelete = async (id) => {
        await remove(id)
        onList();
    }

    const onList = async () => {
        const dataSource = await list();
        setDataSource(dataSource);
      }

      return (
        <div>
            <div className="table-operations">
                <Button onClick={() => setVisible(true)}>新增</Button>
                <ModalForm 
                    visible={visible}  
                    items={items} 
                    onCreate={onCreate} 
                    onCancel={onCancel}
                    onModify={onModify}
                    initialValues={initialValues}
                    mode={mode}>
                </ModalForm>
            </div>
        <Table columns={columns} dataSource={dataSource} rowKey={record => record.id}/>
        </div>
    )
}