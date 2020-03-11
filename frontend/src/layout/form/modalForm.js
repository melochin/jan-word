import React, {useEffect} from 'react';
import { Form, Modal } from 'antd';

const layout = {
    labelCol: { span: 6 },
    wrapperCol: { span: 12 },
  };

export const ModalForm = ({visible, onCreate, onModify, onCancel, items, initialValues, mode}) => {

    const [form] = Form.useForm();

    useEffect(() => {
        form.resetFields();
    })
    
    const onSubmit = values => {
        if (mode != 'modify') {
            onCreate(values);
        } else {
            // 带上ID
            values.id = initialValues.id;
            onModify(values);
        }
      };

      let title = '新增'
      if (mode == 'modify') {
        title = '修改';
      }

    return (
        <Modal
            visible={visible}
            title={title}
            okText='Submit'
            cancelText='Cancel'
            onCancel={onCancel}
            onOk={onSubmit}>
            <Form form={form} onFinish={onSubmit} {...layout} initialValues={initialValues}>
                {items}
            </Form>
        </Modal>
    )
}