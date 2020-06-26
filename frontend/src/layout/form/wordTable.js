import React, {useState, useEffect}from 'react';
import { Form, Input, Button, Popover, Row, Col, List,Modal } from 'antd';
import { Table } from 'antd';
import {ModalForm, useModalForm} from './modalForm';
import {batchAdd, add, remove, list, modify} from '../../action/wordAction.js';
import weblio from '../../action/weblioAction.js';
import {FormList} from '../common/formList';

const { confirm } = Modal;
const { TextArea } = Input;

const layout = {
    labelCol: { 
        xs: { span: 24 },
        sm: { span: 4 },
    },
    wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
    },
  };
  
const WordForm = ({form, onSubmit}) => {

    const [content, setContent] = useState([]);

    const onSearch = (e,val ) => {
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
                <FormList name="sentences"  fieldName="sentence" label="例句" formItemLayout={layout} />
                <Form.Item wrapperCol={{ ...layout.wrapperCol, offset: 6 }} >
                    <Button type="primary" htmlType="submit">提交</Button>
                </Form.Item>
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

const BatchWordForm = ({form, onSubmit}) => {

    return (
        <Row>
            <Col span={24}>
            <Form form={form} 
                labelCol={{ span: 4 }}
                wrapperCol={{ span: 18 }}
                onFinish={(values) => {
                    onSubmit(values)
                }}>
                <Form.Item name="text" label="文本" 
                    rules={[
                        {
                            required: true,
                            message: '请输入日语文本'
                        },
                    ]}>
                        <TextArea rows={10}/>
                </Form.Item>
                <Button type="primary" htmlType="submit">提交</Button>
            </Form>
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
]


export function WordTable() {

    const batchForm = useModalForm();
    const modalForm = useModalForm();
    const [dataSource, setDataSource] = useState([]);
    const [loading, setLoading] = useState(false);
    const [pagination, setPagination] = useState({current:1, pageSize:1, total: 0});

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
                      <Button type='danger' onClick={() => handleConfirm(() => onDelete(record.id))}>删除</Button>
                  </span>
              )
          }
    ];

    const onList = async (params) => {
        setLoading(true);

        if (params == undefined) {
            params = {
              page: pagination.current,
            }
        } 

        const res = await list(params);
        setDataSource(res.dataset);
        setLoading(false);
        setPagination({
            current: res.pageNum,
            pageSize: res.pageSize == 0 ? 1 : res.pageSize,
            total: res.total
          });

    }

    const onBatchAdd = async(values) => {
        await batchAdd(values['text']);
        onList();
        batchForm.onCancel();
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

    const handleTableChange = (pagination, filters) => {
        onList({page: pagination.current});
      };


      return (

        <div>
            <div className="table-operations">
                <Row style={{marginBottom: "10px"}}>
                    <Button onClick={modalForm.setAdd}>新增</Button>
                    <Button onClick={batchForm.setAdd}>批量新增</Button>
                </Row>

                <ModalForm
                    width={800}
                    onAdd={onBatchAdd}
                    {...batchForm}>
                        {(form) => <BatchWordForm {...form} />}
                </ModalForm>


                <ModalForm 
                    width={800}
                    {...modalForm}
                    onAdd={onAdd} 
                    onModify={onModify}>
                        {(form) => <WordForm {...form} />}
                </ModalForm>
            </div>
        <Table columns={columns} dataSource={dataSource} rowKey={record => record.id}
            onChange={handleTableChange}
            loading={loading} pagination={pagination}/>
        </div>
    )
}

const handleConfirm = (deleFunc) => {
    confirm({
      title: '确认删除?',
      okText: 'Yes',
      okType: 'danger',
      cancelText: 'No',
      onOk() {
        deleFunc();
      },
      onCancel() {
    
      },
    });
  }
  