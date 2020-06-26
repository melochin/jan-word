import React, {useState, useEffect} from 'react';
import { Form, Modal } from 'antd';

export const ModalForm = ({visible, onAdd, onModify, onCancel, children, initialValues, mode, width}) => {

    const [form] = Form.useForm();

    useEffect(() => {        
        if (initialValues == null) {
            form.resetFields();
        } else {
            form.setFieldsValue(initialValues);
        }
    })
    
    const onSubmit = values => {
        if (mode != 'modify') {
            onAdd(values);
        } else {
            onModify(values, initialValues);
        }
      };

      let title = '新增'
      if (mode == 'modify') {
        title = '修改';
      }

    return (
        <Modal
            width={width}
            visible={visible}
            title={title}
            footer={null}
            onCancel={onCancel}
            onOk={onSubmit}>
            {children({form, onSubmit})}
        </Modal>
    )
}

export const useModalForm = () => {
    const [visible, setVisible] = useState(false);
    const [initialValues, setInitialValues] = useState(new Object());
    const [mode, setMode] = useState('new');

    const setModify = (record) => {
        setVisible(true);
        setInitialValues(record);
        setMode('modify');
    }

    const setAdd = () => {
        setVisible(true);
        setInitialValues(null);
        setMode('new');
    }

    const onCancel = () => {
        setVisible(false);
    }

    return {
        visible,
        initialValues,
        mode,
        setModify,
        setAdd,
        onCancel
    }

}