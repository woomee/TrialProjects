import { Action, createReducer, on, createFeatureSelector, createSelector} from '@ngrx/store';
import { EntityState, EntityAdapter, createEntityAdapter } from '@ngrx/entity';
// import { Chat } from './chat.model';
import { Comment } from '../../class/chat';
import * as ChatActions from './chat.actions';

export const chatsFeatureKey = 'chats';

export interface State extends EntityState<Comment> {
  // additional entities state properties
  loading: boolean;
}

export const adapter: EntityAdapter<Comment> = createEntityAdapter<Comment>();

export const initialState: State = adapter.getInitialState({
  // additional entity state properties
  loading: false
});

export const reducer = createReducer(
  initialState,
  on(ChatActions.addChat, (state) => {
    return { ...state, loading: true };
  }),
  on(ChatActions.updateChat, (state, action) => {
      return adapter.updateOne(action.payload.chat, { ...state, loading: true });
    }
  ),
  on(ChatActions.deleteChat, (state, action) => {
    return adapter.removeOne(action.payload.id, { ...state, loading: true });
  }),
  on(ChatActions.loadChats, (state) => {
    return { ...state, loading: true };
  }),
  on(ChatActions.loadChatsSuccess, (state, action) => {
    return adapter.upsertMany(action.payload.chats, { ...state, loading: false });
  }),
  on(ChatActions.loadChatsFail, (state) => {
    return { ...state, loading: false };
  }),
  on(ChatActions.writeChatSuccess, (state) => {
    return { ...state, loading: false };
  }),
  on(ChatActions.writeChatFail, (state) => {
    return { ...state, loading: false };
  }),
);

// export const reducer = createReducer(
//   initialState,
//   on(ChatActions.addChat,
//     (state, action) => adapter.addOne(action.chat, state)
//   ),
//   on(ChatActions.upsertChat,
//     (state, action) => adapter.upsertOne(action.chat, state)
//   ),
//   on(ChatActions.addChats,
//     (state, action) => adapter.addMany(action.chats, state)
//   ),
//   on(ChatActions.upsertChats,
//     (state, action) => adapter.upsertMany(action.chats, state)
//   ),
//   on(ChatActions.updateChat,
//     (state, action) => adapter.updateOne(action.chat, state)
//   ),
//   on(ChatActions.updateChats,
//     (state, action) => adapter.updateMany(action.chats, state)
//   ),
//   on(ChatActions.deleteChat,
//     (state, action) => adapter.removeOne(action.id, state)
//   ),
//   on(ChatActions.deleteChats,
//     (state, action) => adapter.removeMany(action.ids, state)
//   ),
//   on(ChatActions.loadChats,
//     (state, action) => adapter.setAll(action.chats, state)
//   ),
//   on(ChatActions.clearChats,
//     state => adapter.removeAll(state)
//   ),
// );


export const {
  selectIds,
  selectEntities,
  selectAll,
  selectTotal,
} = adapter.getSelectors();

export const selectChat = createFeatureSelector<State>('chats');
export const getChatLoading = createSelector(selectChat, state => state.loading);
export const selectAllChats = createSelector(selectChat, selectAll);