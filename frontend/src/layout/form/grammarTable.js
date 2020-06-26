import React, {useState, useEffect} from 'react';
import { Row, Col, Form, Input, Button, Table, Modal} from 'antd';
import TextArea from 'antd/lib/input/TextArea';
import {add, list, remove, modify} from '../../action/grammarAction';
import {ModalForm, useModalForm} from './modalForm';
import {FormList} from '../common/formList';

const { confirm } = Modal;

export const GrammarForm = ({form, onSubmit}) => {

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

  const onFinish = values => {
    onSubmit(values);
  };

  const rulesForSentence = [{
    required: true,
    whitespace: true,
    message: "请输入例句",
  }]

  return (
    <Form form={form} name="dynamic_form_item" {...formItemLayoutWithOutLabel} onFinish={onFinish}>
        <Form.Item name="grammar" label="语法" {...formItemLayout}>
            <Input />
        </Form.Item>
        <Form.Item name="detail" label="说明" {...formItemLayout}>
            <TextArea />
        </Form.Item>
      <FormList name="sentences"  fieldName="sentence" label="例句" rules={rulesForSentence}/>
      <Form.Item>
        <Button type="primary" htmlType="submit">
          Submit
        </Button>
      </Form.Item>
    </Form>
  );
};

const Filter = ({onList}) => {

  const submit = (vals) => {
    onList({page:1, keyword: vals.keyword});
  }

  return (
    <Form layout='inline'  onFinish={submit}>
      <Form.Item name="keyword" label="语法">
        <Input />
      </Form.Item>
      <Form.Item>
        <Button  type="primary" htmlType="submit">检索</Button>
      </Form.Item>
  </Form>
  )
}


export function GrammarTable () {

  const modalForm = useModalForm();
  const [dataSource, setDataSource] = useState([]);
  const [loading, setLoading] = useState(false);
  const [pagination, setPagination] = useState({current:1, pageSize:1, total: 0});
  const [keyword, setKeyword] = useState(null);

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
              <Button type='danger' onClick={() => handleConfirm(() => onDelete(record.id))}>删除</Button>
          </span>
      )
    }
  ]

  const onList = async (params) => {
    setLoading(true);
    
    if (params == undefined) {
      params = {
        page: pagination.current,
        keyword: keyword,
      }
    } else {
      if (params['page'] == undefined) {
        params['page'] = pagination.current;
      }
      if (params['keyword'] == undefined) {
        params['keyword'] = keyword;
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
    setKeyword(params.keyword);
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

  const handleTableChange = (pagination, filters) => {
    onList({page: pagination.current});
  };

  return (
    <div>
      <div className="table-operations">
        <Row style={{marginBottom: "10px"}}>
          <Col flex={2}>
              <Button onClick={modalForm.setAdd}>新增</Button>  
          </Col>
          <Col justify='right' flex={2}>
            <Filter onList={onList}/>
          </Col>
        </Row>
        <ModalForm 
          {...modalForm}
          onAdd={onAdd} 
          onModify={onModify}>
          {(form) => <GrammarForm {...form} />}
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
