import React from 'react';
import { Form, Input, Button } from 'antd';
import { Table } from 'antd';
import {ModalForm} from './modalForm';

import {add, remove, list} from '../../action/wordAction.js';

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
        <Form.Item wrapperCol={{ ...layout.wrapperCol, offset: 6 }}>
            <Button type="primary" htmlType="submit">提交</Button>
        </Form.Item>
    )
]

/**
 * 单词的维护表格（CRUD）
 *
 */
export class WordTable extends React.Component {

    constructor(props) {
        super(props);
        this.state = {dataSource: [], visible: false}
        this.onCancel = this.onCancel.bind(this);
        this.onCreate = this.onCreate.bind(this);
        this.onDelete = this.onDelete.bind(this);
        this.columns  = [
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
                          <Button disabled="disabled" type='primary' style={{marginRight: '5px'}}>修改</Button>
                          <Button type='danger' onClick={() => this.onDelete(record.id)}>删除</Button>
                      </span>
                  )
              }
        ];
    }

    async onCreate(values) {
        await add(values);
        this.onList();
        this.onCancel();
    }

    onCancel = () => {
        this.setState({
            visible: false
        })
      }

    async onDelete(id) {
        await remove(id)
        this.onList();
    }

    async onList() {
        const dataSource = await list();
        console.log(dataSource);
        this.setState({
            dataSource: dataSource
        });
      }

    componentDidMount() {
        this.onList();
    }

    render() {
        return (
            <div>
                <div className="table-operations">
                    <Button onClick={() => this.setState({visible: true})}>新增</Button>
                    <ModalForm visible={this.state.visible} title='新增'  items={items} onCreate={this.onCreate} onCancel={this.onCancel}>
                            <div></div>
                    </ModalForm>
                </div>
            <Table columns={this.columns} dataSource={this.state.dataSource} />
            </div>
        )
    }

}