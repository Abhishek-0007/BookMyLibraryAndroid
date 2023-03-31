package com.example.expensemanager.Utility

class Resource <T> {
    var status :Status = Status.LOADING
    var code :Int ? = null
    var data : T ? =null
    var message :String ? = "Networking error"
    var throwable :Throwable? = null

    private  fun  resource(status: Status, code:Int?, data:T?, message:String?) : Resource<T>{
        this.status = status
        this.code = code
        this.data = data
        this.message = message

        return  this
    }

    fun loading(): Resource<T>{
        return resource(Status.LOADING,null,null,null)
    }
    fun success (data:T?):Resource<T>{
        return resource(Status.SUCCESS, 200, data,"Network Request Success")
    }

    fun error(throwable : Throwable?=null):Resource<T>{
        var message:String?  = throwable?.message
        this.throwable = throwable
        return  resource(Status.ERROR,null,null,message)
    }
}

enum class Status{
    SUCCESS, ERROR, LOADING
}

enum class ConnectUpLoadingStatus{
    DataAvailable, DataNotAvailable, Loading,
    FirstTimeLoading,
    Success,
    Error,
    MoreLoading
}