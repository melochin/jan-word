import React, {useState, useEffect}from 'react';
import { Form, Input, Button, Popover, Row, Col, List } from 'antd';
import { Table } from 'antd';
import {ModalForm, useModalForm} from './modalForm';
import {add, remove, list, modify} from '../../action/wordAction.js';
import weblio from '../../action/weblioAction.js';
import { visible } from 'ansi-colors';

const layout = {
    labelCol: { span: 6 },
    wrapperCol: { span: 12 },
  };
  
const WordForm = ({form, onSubmit}) => {

    const [visible, setVisible] = useState(false);
    const [content, setContent] = useState([]);

    const onSearch = (e,val ) => {
        setVisible(true);
        weblio.getMeanings(form.getFieldValue('word'))
            .then(list => {
                setContent(list);
            });
    }

    return (
        <Row>
            <Col span={12}>
            <Form form={form} onFinish={(values) => {
                onSubmit(values)
                setVisible(false);
            }} {...layout}>
                <Form.Item name="word" label="日语" 
                    validateTrigger={['onChange', 'onBlur']}
                    rules={[
                        {
                            required: true,
                            message: '请输入日语'
                        }, 
                        ({getFieldValue}) => ( {
                            validator: async (rule, value) => {
                                onSearch(value);
                            },
                            validateTrigger: 'onBlur'
                        })
                    ]}>
                        <Input/>
                </Form.Item>
                {items}
            </Form>
            </Col>
            <Col span={12}>
                <List
                bordered
                dataSource={content}
                renderItem={item => (
                <List.Item>
                    {item}
                </List.Item>
                )}
                />
            </Col>
        </Row>
    )
}

const items = [
    (
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
        <Form.Item name="origin" label="来源">
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
              title: '来源',
              dataIndex: 'origin',
              key:'origin',
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
                    width={800}
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