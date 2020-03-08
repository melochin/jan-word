import React from 'react';
import { Form, Modal } from 'antd';

const layout = {
    labelCol: { span: 6 },
    wrapperCol: { span: 12 },
  };

export const ModalForm = ({visible, title, onCreate, onCancel, items, children}) => {

    const [form] = Form.useForm();

    const onSubmit = values => {
        onCreate(values);
        form.resetFields();
      };
    return (
        <Modal
            visible={visible}
            title={title}
            okText='Submit'
            cancelText='Cancel'
            onCancel={onCancel}
            onOk={onSubmit}>
            <Form form={form} onFinish={onSubmit} {...layout}>
                {items}
            </Form>
        </Modal>
    )
}